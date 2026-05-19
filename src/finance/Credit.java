package finance;

public class Credit extends Transaction {
    public enum CreditType { CUSTOMER_PAYMENT, CONTRACT_REFUND }
    private CreditType type;
    
    public Credit(String id, String refId, double amount, String desc, CreditType type) {
        super(id, refId, amount, desc);
        this.type = type;
    }
    
    public CreditType getType() { return type; }
}
