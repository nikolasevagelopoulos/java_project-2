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
        
        for (String part : parts) {
            part = part.trim();
            if (part.contains(":")) {
                String[] keyValue = part.split(":");
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                
                if (key.equalsIgnoreCase("plate")) {
                    this.licensePlate = value;
                } else if (key.equalsIgnoreCase("category")) {
                    this.category = value;
                } else if (key.equalsIgnoreCase("price")) {
                    this.price = Double.parseDouble(value);
                }
            }
        }
    }
}