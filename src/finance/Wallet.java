package finance;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
	public double balance;
	private List<Statement> history;
	
	public Wallet() {
		this.balance = 0.0;
		this.history = new ArrayList<>();
		
	}
	
	public void addTransaction(Transaction tx, String creatorName) {
		if (tx instanceof Charge) {
			balance += tx.getAmount();
		} else if (tx instanceof Credit) {
			balance -= tx.getAmount();
		}
		
		history.add(new Statement(tx, creatorName));
		
	}
	
	public double getBalance() {
		return balance;
	}
	
	public List<Statement> getHistory() {
		return new ArrayList<>(history);
	}

}
