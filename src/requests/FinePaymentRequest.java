package requests;

import java.time.LocalDateTime;

public class FinePaymentRequest extends Request {
	private String vehicleLicensePlate;
	private LocalDateTime noticeDate;
	
	public FinePaymentRequest(String requestId, String vehicleLicensePlate, LocalDateTime noticeDate) {
		super(requestId, vehicleLicensePlate, null);
		this.vehicleLicensePlate = vehicleLicensePlate;
		this.noticeDate = noticeDate;
	}
	
	public String getVehicleLicensePlate() { return vehicleLicensePlate; }
	public LocalDateTime getNoticeeDate() { return noticeDate; }
	
}
