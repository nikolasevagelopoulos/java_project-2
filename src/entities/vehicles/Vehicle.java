package entities.vehicles;

import storage.Storable;

public abstract class Vehicle implements Storable {
    
    protected String licensePlate; // Μοναδική πινακίδα (π.χ. ABC-1234)
    protected String category;     // Economy, Standard, Premium
    protected double price;        // Βασική τιμή (ημερήσια ή μηνιαία ανάλογα το όχημα)

    public Vehicle(String licensePlate, String category, double price) {
        this.licensePlate = licensePlate;
        this.category = category;
        this.price = price;
    }

    // Κενός constructor (χρήσιμος για την fromCSV)
    public Vehicle() {}

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    // Οι υποκλάσεις θα πρέπει να παρέχουν λεπτομέρειες για το αν το κόστος είναι 
    // ημερήσιο ή μηνιαίο, οπότε μπορούμε να βάλουμε μια abstract μέθοδο.
    public abstract String getPriceType();
}