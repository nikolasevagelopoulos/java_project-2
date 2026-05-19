package contracts;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CarRental extends Contract {
	private double dailyRate;
	
	public CarRental(String contractId, String customerVat, String licensePlate, LocalDateTime startDate, LocalDateTime endDate, double dailyRate) {
		
		super(contractId, customerVat, licensePlate, startDate, endDate, calculateEstimatedCost(startDate, endDate, dailyRate ));
		this.dailyRate = dailyRate;	
		}


	private static double calculateEstimatedCost(LocalDateTime start, LocalDateTime end, double dailyRate, long rate) {
		long days = ChronoUnit.DAYS.between(start, end);
		if (days <= 0) days = 1;
		return days * rate;
	}

	@Override
	public String getContractType() {
		return "Car Rental";
	}

	@Override
	public double getRate() {
		return dailyRate;
	}
	

	@Override
	public double getDailyRate() {
		return  dailyRate;
	}
	
	@Override 
	public String toString() {
		return super.toString() + "\n Daily Rate: " + String.format("%.2f", dailyRate);
		
	}
}
