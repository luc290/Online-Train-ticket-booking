
import java.util.ArrayList;

public class TrainCatalog {

    // Sample routes for a college project.
    // These are not verified live train services.

    public static ArrayList<Train> allTrains() {

        ArrayList<Train> list = new ArrayList<>();

        String[][] routes = {

            {"Chandigarh","Delhi","250"},
            {"Chandigarh","Patna","1150"},
            {"Chandigarh","Jaipur","500"},

            {"Delhi","Mumbai","1380"},
            {"Delhi","Kolkata","1450"},
            {"Delhi","Chennai","2180"},
            {"Delhi","Bengaluru","2400"},
            {"Delhi","Hyderabad","1670"},
            {"Delhi","Patna","1000"},
            {"Delhi","Ahmedabad","930"},
            {"Delhi","Lucknow","500"},
            {"Delhi","Varanasi","750"},
            {"Delhi","Guwahati","1900"},

            {"Patna","Mumbai","1700"},
            {"Patna","Kolkata","580"},
            {"Patna","Ranchi","330"},
            {"Patna","Varanasi","250"},
            {"Patna","Bengaluru","2100"},
            {"Patna","Hyderabad","1500"},

            {"Ranchi","Delhi","1200"},
            {"Ranchi","Kolkata","400"},

            {"Mumbai","Bengaluru","980"},
            {"Mumbai","Chennai","1330"},
            {"Mumbai","Hyderabad","710"},
            {"Mumbai","Ahmedabad","490"},
            {"Mumbai","Pune","190"},

            {"Pune","Bengaluru","840"},
            {"Pune","Hyderabad","560"},

            {"Ahmedabad","Jaipur","650"},

            {"Bengaluru","Chennai","350"},
            {"Bengaluru","Hyderabad","570"},
            {"Bengaluru","Kochi","550"},

            {"Chennai","Hyderabad","630"},
            {"Chennai","Kolkata","1660"},

            {"Hyderabad","Kolkata","1200"},

            {"Kolkata","Guwahati","1000"},
            {"Kolkata","Bhubaneswar","440"},
            {"Kolkata","Patna","580"},

            {"Bhopal","Delhi","780"},
            {"Bhopal","Mumbai","780"},

            {"Lucknow","Patna","550"},
            {"Varanasi","Kolkata","680"},
            {"Jaipur","Mumbai","1150"},

            {"Amritsar","Delhi","450"},
            {"Jammu Tawi","Delhi","600"},

            {"Bhubaneswar","Chennai","1200"},
            {"Guwahati","Patna","950"},

            {"Indore","Mumbai","580"},
            {"Surat","Delhi","1100"},
            {"Coimbatore","Chennai","500"},
            {"Mysuru","Bengaluru","140"}
        };

        int number = 12001;

        for (String[] r : routes) {

            int km = Integer.parseInt(r[2]);

            list.add(new Train(
                String.valueOf(number++),
                r[0] + " - " + r[1] + " Express (Demo)",
                r[0],
                r[1],
                km
            ));

            list.add(new Train(
                String.valueOf(number++),
                r[1] + " - " + r[0] + " Express (Demo)",
                r[1],
                r[0],
                km
            ));
        }

        return list;
    }
}
