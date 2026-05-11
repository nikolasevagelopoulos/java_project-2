package entities.vehicles;


public class PassengerCar extends Vehicle {

    public PassengerCar(String licensePlate, String category, double dailyPrice) {
        super(licensePlate, category, dailyPrice);
    }

    public PassengerCar() {
        super();
    }

    @Override
    public String getPriceType() {
        return "Daily"; // Δηλώνει ότι η τιμή είναι ανά ημέρα
    }

    @Override
    public String toCSV() {
        // Παράδειγμα: "PASSENGER,ABC-1234,Economy,35.0"
        return "PASSENGER," + licensePlate + "," + category + "," + price;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.licensePlate = parts[1];
        this.category = parts[2];
        this.price = Double.parseDouble(parts[3]);
    }
}