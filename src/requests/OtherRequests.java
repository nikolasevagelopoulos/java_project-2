package requests;

public class OtherRequests {
 
    public static class RentalCancelationRequest extends Request {
        public RentalCancelationRequest(String requestId, String customerVat, String contractId) {
            super(requestId, contractId, customerVat);
        }
        
        @Override
        public String getType() { return "CANCEL"; }
    }
    
    public static class RentalReturnRequest extends Request {
        public RentalReturnRequest(String requestId, String customerVat, String contractId) {
            super(requestId, contractId, customerVat);
        }
        
        @Override
        public String getType() { return "RETURN"; }
    }
    
    public static class CustomerPaymentRequest extends Request {
        public CustomerPaymentRequest(String requestId, String customerVat) {
            super(requestId, null, customerVat);
        }

        @Override
        public String getType() { return "PAYMENT"; }
    }
}
