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
            if (part.contains(":")) {
                String[] kv = part.split(":");
                String key = kv[0].trim();
                String val = kv.length > 1 ? kv[1].trim() : "";
                
                if (key.equalsIgnoreCase("plate")) this.licensePlate = val;
                else if (key.equalsIgnoreCase("category")) this.category = val;
            }
        }
        
        // ΟΙ ΕΛΕΓΧΟΙ ΜΠΑΙΝΟΥΝ ΕΔΩ, ΕΞΩ ΑΠΟ ΤΟ LOOP!
        if (this.category != null) {
            if (this.category.equalsIgnoreCase("Premium")) this.price = 100.0;
            else if (this.category.equalsIgnoreCase("Standard")) this.price = 50.0;
            else this.price = 30.0;
        } else {
            this.price = 30.0; // Default τιμή αν για κάποιο λόγο δεν βρέθηκε κατηγορία
        }
    }
}