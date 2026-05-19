package requests;

import java.time.LocalDateTime;

public abstract class Request {
	private String requestId;
	private String referenceId;
	private LocalDateTime timestamp;
	private String customerVat;
	
	public Request(String requestId, String referenceId, String customerVat) {
		this.requestId = requestId;
		this.referenceId = referenceId;
		this.customerVat = customerVat;
		this.timestamp = LocalDateTime.now();
	}
	
	public String getRequestId() { return requestId; }
	public String getReferenceId() { return referenceId; }
	public LocalDateTime getTimestamp() { return timestamp; }
	public String getCustomerVat() { return customerVat; }

}
