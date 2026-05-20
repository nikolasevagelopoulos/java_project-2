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
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                String key = kv[0].trim();
                String val = kv.length > 1 ? kv[1].trim() : "";
                
                if (key.equalsIgnoreCase("plate") || key.equalsIgnoreCase("vehicleLicensePlate")) {
                    this.vehicleLicensePlate = val;
                } else if (key.equalsIgnoreCase("amount")) {
                    if (!val.isEmpty()) {
                        this.amount = Double.parseDouble(val);
                    }
                } else if (key.equalsIgnoreCase("date") || key.equalsIgnoreCase("noticeDate")) {
                    if (!val.isEmpty()) {
                        // Μετατροπή της ημερομηνίας του προστίμου σε LocalDateTime
                        this.noticeDate = java.time.LocalDate.parse(val).atStartOfDay();
                    }
                }
            }
        }
    }
}