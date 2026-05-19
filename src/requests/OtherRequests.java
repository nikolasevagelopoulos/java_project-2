package requests;

import java.time.LocalDateTime;

public class OtherRequests {
 
    public static class RentalCancelationRequest extends Request {
        public RentalCancelationRequest(String requestId, String customerVat, String contractId) {
            super(requestId, contractId, customerVat);
        }
        
        public RentalCancelationRequest() { super(); }
        
        @Override
        public String getType() { return "CANCEL"; }

        @Override
        public String toCSV() {
            return "CANCEL," + requestId + "," + referenceId + "," + timestamp + "," + customerVat;
        }

        @Override
        public void fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");
            this.requestId = parts[1];
            this.referenceId = parts[2];
            this.timestamp = LocalDateTime.parse(parts[3]);
            this.customerVat = parts[4];
        }
    }
    
    public static class RentalReturnRequest extends Request {
        public RentalReturnRequest(String requestId, String customerVat, String contractId) {
            super(requestId, contractId, customerVat);
        }
        
        public RentalReturnRequest() { super(); }
        
        @Override
        public String getType() { return "RETURN"; }

        @Override
        public String toCSV() {
            return "RETURN," + requestId + "," + referenceId + "," + timestamp + "," + customerVat;
        }

        @Override
        public void fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");
            this.requestId = parts[1];
            this.referenceId = parts[2];
            this.timestamp = LocalDateTime.parse(parts[3]);
            this.customerVat = parts[4];
        }
    }
    
    public static class CustomerPaymentRequest extends Request {
        private double amount; // Προσθήκη του ποσού
        
        public CustomerPaymentRequest(String requestId, String customerVat, double amount) {
            super(requestId, null, customerVat);
            this.amount = amount;
        }
        
        public CustomerPaymentRequest() { super(); }
        
        public double getAmount() { return amount; } // Getter για τον RequestProcessor
        
        @Override
        public String getType() { return "PAYMENT"; }

        @Override
        public String toCSV() {
            return "PAYMENT," + requestId + "," + timestamp + "," + customerVat + "," + amount;
        }

        @Override
        public void fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");
            this.requestId = parts[1];
            this.timestamp = LocalDateTime.parse(parts[2]);
            this.customerVat = parts[3];
            this.amount = Double.parseDouble(parts[4]);
        }
    }
    }
