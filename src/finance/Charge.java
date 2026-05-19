package finance;

public class Charge extends Transaction {
	public enum ChargeType { RENTAL, OVERDUE, FINE }
	private ChargeType type;
	
	public Charge(String id, String refId, double amount, String desc, ChargeType type) {
		super(id, refId, amount, desc);
		this.type = type;
	}
	
	public ChargeType getType() { return type; }
	
}
