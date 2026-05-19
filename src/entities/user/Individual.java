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
        
        this.username = parts[1].trim();
        this.password = parts[2].trim();
        this.vat = parts[3].trim();
    }
}
