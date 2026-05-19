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
        this.role = "ADMIN";
        
        for (String part : parts) {
            part = part.trim();
            if (part.contains(":")) {
                String[] keyValue = part.split(":");
                if (keyValue.length > 1) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    
                    if (key.equalsIgnoreCase("username")) {
                        this.username = value;
                    } else if (key.equalsIgnoreCase("password")) {
                        this.password = value;
                    }
                }
            }
        }
    }
}