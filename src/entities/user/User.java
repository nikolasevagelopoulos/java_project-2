package entities.user;

import storage.Storable;

public abstract class User implements Storable {
    
    protected String username;
    protected String password;
    protected String role; // π.χ. "ADMIN", "INDIVIDUAL", "COMPANY" για να μας βοηθάει στο CSV

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Γράφουμε default κενό constructor, συχνά χρειάζεται όταν φτιάχνουμε κενά 
    // αντικείμενα για να καλέσουμε την fromCSV()
    public User() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }

    // Η υλοποίηση της toCSV / fromCSV μπορεί να γίνει και στις υποκλάσεις, 
    // αλλά μια βασική υλοποίηση εδώ είναι χρήσιμη.
}