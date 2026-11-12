
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataManager {

    private static final String USERS = "data/users.txt";
    private static final String BOOKINGS = "data/bookings.txt";

    public static final String[] CLASSES = {
        "Sleeper (SL)",
        "AC 3 Tier (3A)",
        "AC 2 Tier (2A)",
        "AC First Class (1A)"
    };

    public static final String[] QUOTAS = {
        "General", "Tatkal"
    };

    private static final String[] PREFIX = {"S", "B", "A", "H"};

    private static final int[] CAPACITY = {
        72, 64, 46, 24
    };

    private static final int RAC_LIMIT = 10;

    public static void initialize() {

        new File("data").mkdirs();

        try {
            new File(USERS).createNewFile();
            new File(BOOKINGS).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String enc(String s) {
        return Base64.getEncoder().encodeToString(
            s.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String dec(String s) {
        return new String(
            Base64.getDecoder().decode(s),
            StandardCharsets.UTF_8
        );
    }

    private static List<String> read(String file) {

        try {
            return java.nio.file.Files.readAllLines(
                new File(file).toPath(),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static void append(String file, String line) {

        try (FileWriter w = new FileWriter(file, true)) {
            w.write(line + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static synchronized boolean register(
            String user, String pass) {

        if (user == null || pass == null ||
            user.trim().isEmpty() || pass.isEmpty()) {
            return false;
        }

        for (String line : read(USERS)) {

            String[] p = line.split("\\|", -1);

            if (p.length == 2 &&
                dec(p[0]).equalsIgnoreCase(user.trim())) {
                return false;
            }
        }

        append(USERS, enc(user.trim()) + "|" + enc(pass));

        return true;
    }

    public static boolean login(String user, String pass) {

        for (String line : read(USERS)) {

            String[] p = line.split("\\|", -1);

            if (p.length == 2 &&
                dec(p[0]).equals(user.trim()) &&
                dec(p[1]).equals(pass)) {
                return true;
            }
        }

        return false;
    }

    private static String[] fields(Booking b) {

        return new String[] {
            b.id, b.username, b.trainNumber, b.trainName,
            b.source, b.destination, b.passenger,
            String.valueOf(b.age), b.date, b.travelClass,
            b.quota, b.coach, b.seat, b.status,
            String.valueOf(b.fare)
        };
    }

    private static String serialize(Booking b) {

        StringJoiner j = new StringJoiner("|");

        for (String s : fields(b)) {
            j.add(enc(s));
        }

        return j.toString();
    }

    private static Booking parse(String line) {

        try {

            String[] p = line.split("\\|", -1);

            if (p.length != 15) {
                return null;
            }

            String[] v = new String[15];

            for (int i = 0; i < p.length; i++) {
                v[i] = dec(p[i]);
            }

            return new Booking(
                v[0], v[1], v[2], v[3], v[4],
                v[5], v[6], Integer.parseInt(v[7]),
                v[8], v[9], v[10], v[11], v[12],
                v[13], Integer.parseInt(v[14])
            );

        } catch (Exception e) {
            return null;
        }
    }

    private static List<Booking> allBookings() {

        List<Booking> out = new ArrayList<>();

        for (String line : read(BOOKINGS)) {

            Booking b = parse(line);

            if (b != null) {
                out.add(b);
            }
        }

        return out;
    }

    private static int classIndex(String c) {

        for (int i = 0; i < CLASSES.length; i++) {

            if (CLASSES[i].equals(c)) {
                return i;
            }
        }

        return -1;
    }

    // Estimated fare for this educational project.
    // These are not official railway fares.

    public static int fare(
            Train train, String travelClass, String quota) {

        int i = classIndex(travelClass);

        if (i < 0) {
            return 0;
        }

        double[] rate = {
            0.75, 1.25, 1.85, 3.10
        };

        int[] reservation = {
            20, 40, 60, 100
        };

        int base = (int) Math.round(
            train.distanceKm * rate[i] + reservation[i]
        );

        base = Math.max(base, 150);

        if ("Tatkal".equals(quota)) {

            int[] extra = {
                150, 350, 450, 0
            };

            base += extra[i];
        }

        return base;
    }

    private static boolean sameJourney(
            Booking b, Train t, String date,
            String cls, String quota) {

        return b.trainNumber.equals(t.number)
            && b.date.equals(date)
            && b.travelClass.equals(cls)
            && b.quota.equals(quota)
            && !b.status.equals("CANCELLED");
    }

    public static synchronized int confirmedAvailable(
            Train t, String date, String cls, String quota) {

        int i = classIndex(cls);

        if (i < 0 ||
            ("Tatkal".equals(quota) && i == 3)) {
            return 0;
        }

        int used = 0;

        for (Booking b : allBookings()) {

            if (sameJourney(b, t, date, cls, quota)
                && b.status.equals("CONFIRMED")) {
                used++;
            }
        }

        int capacity = "Tatkal".equals(quota)
            ? Math.max(1, CAPACITY[i] / 5)
            : CAPACITY[i];

        return Math.max(0, capacity - used);
    }

    public static synchronized int racAvailable(
            Train t, String date, String cls, String quota) {

        int used = 0;

        for (Booking b : allBookings()) {

            if (sameJourney(b, t, date, cls, quota)
                && b.status.startsWith("RAC")) {
                used++;
            }
        }

        return Math.max(0, RAC_LIMIT - used);
    }

    public static synchronized String availabilityText(
            Train t, String date, String cls, String quota) {

        int confirmed = confirmedAvailable(
            t, date, cls, quota
        );

        int rac = racAvailable(
            t, date, cls, quota
        );

        int waiting = 0;

        for (Booking b : allBookings()) {

            if (sameJourney(b, t, date, cls, quota)
                && b.status.startsWith("WL")) {
                waiting++;
            }
        }

        return "Confirmed: " + confirmed +
               " | RAC: " + rac +
               " | Waiting: " + waiting;
    }

    public static synchronized Booking book(
            String user, Train t, String passenger,
            int age, String date, String cls, String quota) {

        int i = classIndex(cls);

        if (i < 0) {
            throw new IllegalArgumentException(
                "Select a valid class."
            );
        }

        if ("Tatkal".equals(quota) && i == 3) {
            throw new IllegalArgumentException(
                "Tatkal is not available in First AC (1A)."
            );
        }

        int totalCapacity = "Tatkal".equals(quota)
            ? Math.max(1, CAPACITY[i] / 5)
            : CAPACITY[i];

        int confirmedUsed = totalCapacity -
            confirmedAvailable(t, date, cls, quota);

        int racUsed = RAC_LIMIT -
            racAvailable(t, date, cls, quota);

        String status;
        String coach;
        String seat;

        if (confirmedAvailable(t, date, cls, quota) > 0) {

            status = "CONFIRMED";

            int coachNo = confirmedUsed / 24 + 1;

            coach = PREFIX[i] + coachNo;

            seat = String.valueOf(
                (confirmedUsed % 24) + 1
            );

        } else if (racAvailable(t, date, cls, quota) > 0) {

            status = "RAC";
            coach = "RAC";
            seat = "RAC-" + (racUsed + 1);

        } else {

            int wl = 1;

            for (Booking b : allBookings()) {

                if (sameJourney(b, t, date, cls, quota)
                    && b.status.startsWith("WL")) {
                    wl++;
                }
            }

            status = "WAITING";
            coach = "WL";
            seat = "WL-" + wl;
        }

        String id = "RE" + System.currentTimeMillis();

        Booking b = new Booking(
            id, user, t.number, t.name,
            t.source, t.destination, passenger,
            age, date, cls, quota, coach,
            seat, status, fare(t, cls, quota)
        );

        append(BOOKINGS, serialize(b));

        return b;
    }

    public static List<Booking> userBookings(String user) {

        List<Booking> result = new ArrayList<>();

        for (Booking b : allBookings()) {

            if (b.username.equals(user)) {
                result.add(b);
            }
        }

        return result;
    }

    public static synchronized boolean cancel(
            String id, String user) {

        List<Booking> all = allBookings();

        boolean changed = false;

        try (PrintWriter out =
                new PrintWriter(new FileWriter(BOOKINGS))) {

            for (Booking b : all) {

                if (b.id.equals(id) &&
                    b.username.equals(user) &&
                    !b.status.equals("CANCELLED")) {

                    b.status = "CANCELLED";
                    changed = true;
                }

                out.println(serialize(b));
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return changed;
    }
}
