package requests;

import java.time.LocalDateTime;

public class FinePaymentRequest extends Request {
    private String vehicleLicensePlate;
    private LocalDateTime noticeDate;
    private double amount;
    
    public FinePaymentRequest(String requestId, String referenceId, String vehicleLicensePlate, LocalDateTime noticeDate, double amount) {
        super(requestId, referenceId, null); // VAT is tracked indirectly through licensePlate and date [cite: 65]
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.noticeDate = noticeDate;
        this.amount = amount;
    }
    
    public String getVehicleLicensePlate() { return vehicleLicensePlate; }
    public LocalDateTime getNoticeDate() { return noticeDate; }
    public double getAmount() { return amount; }

    @Override
    public String getType() {
        return "FINE";
    }
}
