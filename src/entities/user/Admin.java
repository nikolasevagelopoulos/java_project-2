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
        
        this.username = parts[1].trim();
        this.password = parts[2].trim();
    }
    
}