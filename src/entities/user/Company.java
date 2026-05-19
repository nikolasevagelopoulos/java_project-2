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
        
        this.username = parts[1].trim();
        this.password = parts[2].trim();
        this.vat = parts[3].trim();
    }
    
}