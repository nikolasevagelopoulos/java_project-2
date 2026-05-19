package requests;

import java.time.LocalDateTime;

public class FinePaymentRequest extends Request {
    private String vehicleLicensePlate;
    private LocalDateTime noticeDate;
    private double amount;
    
    public FinePaymentRequest(String requestId, String referenceId, String vehicleLicensePlate, LocalDateTime noticeDate, double amount) {
        super(requestId, referenceId, null);
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.noticeDate = noticeDate;
        this.amount = amount;
    }
    
    public FinePaymentRequest() {
        super();
    }
    
    public String getVehicleLicensePlate() { return vehicleLicensePlate; }
    public LocalDateTime getNoticeDate() { return noticeDate; }
    public double getAmount() { return amount; }

    @Override
    public String getType() {
        return "FINE";
    }

    @Override
    public String toCSV() {
        return "FINE," + requestId + "," + referenceId + "," + timestamp + "," + vehicleLicensePlate + "," + noticeDate + "," + amount;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.requestId = parts[1];
        this.referenceId = parts[2];
        this.timestamp = LocalDateTime.parse(parts[3]);
        this.vehicleLicensePlate = parts[4];
        this.noticeDate = LocalDateTime.parse(parts[5]);
        this.amount = Double.parseDouble(parts[6]);
    }
}