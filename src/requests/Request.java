package requests;

import java.time.LocalDateTime;
import storage.Storable;

public abstract class Request implements Storable {
    protected String requestId;
    protected String referenceId;
    protected LocalDateTime timestamp;
    protected String customerVat;
    
    public Request(String requestId, String referenceId, String customerVat) {
        this.requestId = requestId;
        this.referenceId = referenceId;
        this.customerVat = customerVat;
        this.timestamp = LocalDateTime.now();
    }
    
    // Default constructor for fromCSV restoration
    public Request() {}
    
    public String getRequestId() { return requestId; }
    public String getReferenceId() { return referenceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCustomerVat() { return customerVat; }
    
    public abstract String getType();
}