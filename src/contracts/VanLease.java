package contracts;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class VanLease extends Contract {
	private double monthlyRate;
	public VanLease(String contractId, String customerVat, String licensePlate, LocalDateTime startDate, LocalDateTime endDate, double monthlyRate) {
		super(contractId, customerVat, licensePlate, startDate, endDate, calculateEstimatedCost(startDate, endDate, monthlyRate), licensePlate, endDate, endDate, monthlyRate);
	}

	private static double calculateEstimatedCost(LocalDateTime start, LocalDateTime end, double rate) {
		
		long months = ChronoUnit.MONTHS.between(start, end);
		if (months <= 0) months = 1;
		return months * rate;
	}

	@Override
	public String getContractType() {
		return "Van Lease";
	}

	@Override
	public double getRate() {
		return monthlyRate;
	}

	@Override
	public double getDailyRate() {
		return monthlyRate / 30.0;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\n Monthly Rate: " + String.format("%.2f", monthlyRate);
	}

}
