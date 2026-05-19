package entities.user;

public class Company extends Customer {
    
    public Company(String username, String password, String vat) {
        super(username, password, "COMPANY", vat);
    }

    public Company() {
        super();
    }

    @Override
    public String toCSV() {
        return "COMPANY," + username + "," + password + "," + vat;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.role = "COMPANY";
        
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