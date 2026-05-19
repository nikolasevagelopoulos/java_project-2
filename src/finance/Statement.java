package finance;

import java.time.LocalDateTime;

public class Statement {
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
	
	@Override
	public String toString() {
		return String.format("[%s] %s | %s | Ποσό: %.2f | Από: %s", timestamp, type, description, amount, creator);
		
	}
}
