package finance;

import java.time.LocalDateTime;
import storage.Storable;

public class Statement implements Storable {
    private LocalDateTime timestamp;
    private String type; 
    private String creator;
    private double amount;
    private String description;
    private String referenceId;
    
    public Statement(Transaction tx, String creator) {
        this.timestamp = tx.getTimestamp();
        this.amount = tx.getAmount();
        this.description = tx.getDescription();
        this.referenceId = tx.getReferenceId();
        this.creator = creator;
        this.type = (tx instanceof Charge) ? "Charge" : "Credit";
    }
    
    // Κενός constructor απαραίτητος για την fromCSV (ανάκτηση δεδομένων)
    public Statement() {}
    
    @Override
    public String toCSV() {
        return timestamp + "," + type + "," + creator + "," + amount + "," + description + "," + referenceId;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.timestamp = LocalDateTime.parse(parts[0]);
        this.type = parts[1];
        this.creator = parts[2];
        this.amount = Double.parseDouble(parts[3]);
        this.description = parts[4];
        this.referenceId = parts[5];
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s | %s | Ποσό: %.2f | Από: %s", timestamp, type, description, amount, creator);
    }
}