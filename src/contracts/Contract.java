package contracts;

import java.time.LocalDateTime;

public  abstract class Contract {
	protected String contractId;
	protected String customerVat;
	protected String licensePlate;
	protected LocalDateTime startDate;
	protected LocalDateTime endDate;
	protected double estimatedCost;
	protected LocalDateTime creationDate;
	
	public Contract(String contractId, String customervat, String licensePlate, LocalDateTime startdate, LocalDateTime enddate, double estimatesCost, String customerVat, LocalDateTime startDate, LocalDateTime endDate, double estimatedCost) {
		this.contractId = contractId;
		this.customerVat = customerVat;
		this.licensePlate = licensePlate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.estimatedCost = estimatedCost;
		this.creationDate = LocalDateTime.now();
	}

		
	
	public String getContractId() { return contractId; }
	public String getCustomerVat() { return customerVat; }
	public String getLicensePlate() { return licensePlate; }
	public LocalDateTime getStartDate() { return startDate; }
	public LocalDateTime getEndDate() { return endDate; }
	public double getEstimatedCost() { return estimatedCost; }
	public LocalDateTime getCreationDate() { return creationDate; }
	
	public abstract String getContractType() ;
	
	public abstract double getRate();
	
	public abstract double getDailyRate();
	
	@Override
	public String toString() {
		return "Contract ID: " + contractId +
			"\n Type: " + getContractType() +
			"\n Customer VAT: " + customerVat +
			"\n Vehicle Plate: " + licensePlate +
			"\n Dates: " + startDate.toLocalDate() + " to " + endDate.toLocalDate() +
			"\n Estimated Cost: " + String.format("%.2f", estimatedCost) +
			"\n Created On: " + creationDate.toLocalDate();
		}	
}
