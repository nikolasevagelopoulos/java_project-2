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
        this.role = parts[0];
        this.username = parts[1];
        this.password = parts[2];
        this.vat = parts[3];
    }
}
