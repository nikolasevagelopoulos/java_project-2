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
        // Αποθήκευση με τη σωστή σειρά: Τύπος, Πινακίδα, Κατηγορία, Τιμή
        return "PASSENGER," + licensePlate + "," + category + "," + price;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        
        this.licensePlate = parts[1].trim();
        this.category = parts[2].trim();
        this.price = Double.parseDouble(parts[3].trim());
    }
}