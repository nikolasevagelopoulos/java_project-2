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
        this.role = parts[0];
        this.username = parts[1];
        this.password = parts[2];
    }
}
