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
        return "VAN," + licensePlate + "," + category + "," + price;
    }

    
   
    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        
        this.licensePlate = parts[1].trim();
        this.category = parts[2].trim();
        this.price = Double.parseDouble(parts[3].trim());
    }
}
