
public class Train {
    public String number, name, source, destination;
    public int distanceKm;

    public Train(String number, String name, String source,
                 String destination, int distanceKm) {
        this.number = number;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public String toString() {
        return number + " | " + name + " | " +
               source + " to " + destination;
    }
}
