package entities.user;

public class Individual extends Customer {
    
    public Individual(String username, String password, String vat) {
        super(username, password, "INDIVIDUAL", vat);
    }

    public Individual() {
        super();
    }

    @Override
    public String toCSV() {
        return "INDIVIDUAL," + username + "," + password + "," + vat;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.role = "INDIVIDUAL";
        
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
                    } else if (key.equalsIgnoreCase("vat")) {
                        this.vat = value;
                    }
                }
            }
        }
    }
}