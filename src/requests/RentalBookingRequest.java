package requests;

import java.time.LocalDateTime;

public class RentalBookingRequest extends Request {
	private String vehicleLicensePlate;
	private LocalDateTime rentalStartDate;
	private LocalDateTime rentalEndDate;
	
	public RentalBookingRequest(String requestId, String customerVat, String vehicleLicensePlate, LocalDateTime start, LocalDateTime end) {
		super(requestId, vehicleLicensePlate, customerVat);
		this.vehicleLicensePlate = vehicleLicensePlate;
		this.rentalStartDate = start;
		this.rentalEndDate = end;
	}
	
	public String getVehicleLicensePlate() { return vehicleLicensePlate; }
	public LocalDateTime getRentalStartDate() { return rentalStartDate; } 
	public LocalDateTime getRentalEndDate() { return rentalEndDate; } 
	
}
