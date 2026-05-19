package requests;

public class OtherRequests {
 
	   public static class RentalCancelationRequest extends Request {
		   private String contractId;
		   
		   public RentalCancelationRequest(String requestId, String customerVat, String contractId) {
			   super(requestId, contractId, customerVat);
			   this.contractId = contractId;
		   }
		   
		   public String getContractId() { return contractId; }
		   
	   }
	   
	   public static class RentalReturnRequest extends Request {
		   private String contractId;
		   
		   public RentalReturnRequest(String requestId, String customerVat, String contractId) {
			   super(requestId, contractId, customerVat);
			   this.contractId = contractId;
		   }
		   
		   public String getContractId() { return contractId; }
	   }
	   
	   public static class CustomerPaymentRequest extends Request {
		    public CustomerPaymentRequest(String requestId, String customerVat) {
		    	super(requestId, null, customerVat);
		    }
	   }
}
