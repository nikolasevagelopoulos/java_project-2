package entities.user;

public class Admin extends User {
    
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }
    
    public Admin() {
        super();
    }

    @Override
    public String toCSV() {
        return "ADMIN," + username + "," + password;
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
            }
        }
        // Αν το αρχείο έχει κενό username, βάζουμε το "admin"
        if (this.username == null || this.username.isEmpty()) {
            this.username = "admin";
            this.password = "1234";
        }
    }
    
}