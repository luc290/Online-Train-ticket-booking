
public class Booking {
    public String id, username, trainNumber, trainName;
    public String source, destination, passenger, date;
    public String travelClass, quota, coach, seat, status;
    public int age, fare;

    public Booking(String id, String username, String trainNumber,
                   String trainName, String source, String destination,
                   String passenger, int age, String date,
                   String travelClass, String quota, String coach,
                   String seat, String status, int fare) {

        this.id = id;
        this.username = username;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.passenger = passenger;
        this.age = age;
        this.date = date;
        this.travelClass = travelClass;
        this.quota = quota;
        this.coach = coach;
        this.seat = seat;
        this.status = status;
        this.fare = fare;
    }
}
