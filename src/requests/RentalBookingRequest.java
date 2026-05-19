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
        this.requestId = parts[1];
        this.referenceId = parts[2];
        this.timestamp = LocalDateTime.parse(parts[3]);
        this.customerVat = parts[4];
        this.vehicleCategory = parts[5];
        this.rentalStartDate = LocalDateTime.parse(parts[6]);
        this.rentalEndDate = LocalDateTime.parse(parts[7]);
    }
}