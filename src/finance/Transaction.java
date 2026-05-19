package finance;

import java.time.LocalDateTime;

public abstract class Transaction {
    private String transactionId;
    private String referenceId;
    private LocalDateTime timestamp;
    private double amount;
    private String description;
    
    public Transaction(String transactionId, String referenceId, double amount, String description) {
        this.transactionId = transactionId;
        this.referenceId = referenceId;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    public double getAmount() { return amount; }
    public String getTransactionId() { return transactionId; }
    public String getReferenceId() { return referenceId; } 
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
