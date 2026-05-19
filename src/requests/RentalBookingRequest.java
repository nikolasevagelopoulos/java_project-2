package requests;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalBookingRequest extends Request {
    private String vehicleCategory; // Economy, Standard, Premium [cite: 70]
    private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;
    
    public RentalBookingRequest(String requestId, String customerVat, String referenceId, String vehicleCategory, LocalDateTime start, LocalDateTime end) {
        super(requestId, referenceId, customerVat);
        this.vehicleCategory = vehicleCategory;
        this.rentalStartDate = start;
        this.rentalEndDate = end;
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
}
