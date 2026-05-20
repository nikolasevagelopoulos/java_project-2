package requests;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalBookingRequest extends Request {
    private String vehicleCategory;
    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;
    
    public RentalBookingRequest(String requestId, String customerVat, String referenceId, String vehicleCategory, LocalDateTime start, LocalDateTime end) {
        super(requestId, referenceId, customerVat);
        this.vehicleCategory = vehicleCategory;
        this.rentalStartDate = start;
        this.rentalEndDate = end;
    }
    
    public RentalBookingRequest() {
        super();
    }
    
    public String getVehicleCategory() { return vehicleCategory; }
    public LocalDateTime getRentalStartDate() { return rentalStartDate; } 
    public LocalDateTime getRentalEndDate() { return rentalEndDate; } 
    
    public long getDuration() {
        long days = ChronoUnit.DAYS.between(rentalStartDate, rentalEndDate);
        return days <= 0 ? 1 : days;
    }

    @Override
    public String getType() {
        return "BOOKING";
    }

    @Override
    public String toCSV() {
        return "BOOKING," + requestId + "," + referenceId + "," + timestamp + "," + customerVat + "," + vehicleCategory + "," + rentalStartDate + "," + rentalEndDate;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                String key = kv[0].trim();
                String val = kv.length > 1 ? kv[1].trim() : "";
                
                if (key.equalsIgnoreCase("customer") || key.equalsIgnoreCase("customerVat")) {
                    this.customerVat = val;
                } else if (key.equalsIgnoreCase("category") || key.equalsIgnoreCase("vehicleCategory")) {
                    this.vehicleCategory = val;
                } else if (key.equalsIgnoreCase("start") || key.equalsIgnoreCase("rentalStartDate")) {
                    // Αν το CSV έχει ημερομηνία σε στυλ YYYY-MM-DD, τη μετατρέπουμε σε LocalDateTime
                    if (!val.isEmpty()) {
                        this.rentalStartDate = java.time.LocalDate.parse(val).atStartOfDay();
                    }
                } else if (key.equalsIgnoreCase("end") || key.equalsIgnoreCase("rentalEndDate")) {
                    if (!val.isEmpty()) {
                        this.rentalEndDate = java.time.LocalDate.parse(val).atStartOfDay();
                    }
                }
            }
        }
    }
}