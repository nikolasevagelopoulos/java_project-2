package entities.vehicles;


public class CommercialVan extends Vehicle {

    public CommercialVan(String licensePlate, String category, double monthlyPrice) {
        super(licensePlate, category, monthlyPrice);
    }

    public CommercialVan() {
        super();
    }

    @Override
    public String getPriceType() {
        return "Monthly"; // Δηλώνει ότι η τιμή είναι ανά μήνα
    }

    @Override
    public String toCSV() {
        // Παράδειγμα: "VAN,XYZ-9876,Premium,400.0"
        return "VAN," + licensePlate + "," + category + "," + price;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.licensePlate = parts[1];
        this.category = parts[2];
        this.price = Double.parseDouble(parts[3]);
    }
}