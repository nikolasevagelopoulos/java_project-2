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
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                String key = kv[0].trim();
                String val = kv.length > 1 ? kv[1].trim() : "";
                
                if (key.equalsIgnoreCase("plate")) this.licensePlate = val;
                else if (key.equalsIgnoreCase("category")) this.category = val;
            }
        }
        
        // Έξω από το loop για ασφάλεια
        if (this.category != null) {
            if (this.category.equalsIgnoreCase("Premium")) this.price = 100.0;
            else if (this.category.equalsIgnoreCase("Standard")) this.price = 50.0;
            else this.price = 30.0;
        } else {
            this.price = 50.0;
        }
    }
}
