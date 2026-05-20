package entities.user;

public class Individual extends Customer {
    
    public Individual(String username, String password, String vat) {
        super(username, password, "INDIVIDUAL", vat);
    }

    public Individual() {
        super();
        this.wallet = new finance.Wallet(); // Δημιουργούμε το wallet
        this.wallet.balance = 5000.0;       // Τους δίνουμε 5000€ αρχικό υπόλοιπο
    }
    

    @Override
    public String toCSV() {
        return "INDIVIDUAL," + username + "," + password + "," + vat;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                String key = kv[0].trim();
                String val = kv.length > 1 ? kv[1].trim() : "";
                
                if (key.equalsIgnoreCase("username")) this.username = val;
                else if (key.equalsIgnoreCase("password")) this.password = val;
                else if (key.equalsIgnoreCase("vat")) this.vat = val;
                else if (key.equalsIgnoreCase("balance")) {
                	if (this.wallet != null) {
                        this.wallet.balance = Double.parseDouble(val);
                    }
                }
                // Αν το αρχείο δεν έχει username (όπως βλέπω), χρησιμοποιούμε το VAT ως username για να μπορούν να συνδεθούν!
                if (this.username == null || this.username.isEmpty()) {
                    this.username = this.vat;
                }
            }
        }
    }
}
