package contracts;

import java.time.LocalDateTime;
import storage.Storable; // Import the Storable interface

public abstract class Contract implements Storable { // Implement Storable here
    protected String contractId;
    protected String customerVat;
    protected String licensePlate;
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    protected double estimatedCost;
    protected LocalDateTime creationDate;
    protected String actualEndDate; // "PENDING", "CANCELED_...", or return date
    
    public Contract(String contractId, String customerVat, String licensePlate, LocalDateTime startDate, LocalDateTime endDate, double estimatedCost) {
        this.contractId = contractId;
        this.customerVat = customerVat;
        this.licensePlate = licensePlate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedCost = estimatedCost;
        this.creationDate = LocalDateTime.now();
        this.actualEndDate = "PENDING";
    }
    
    public String getContractId() { return contractId; }
    public String getCustomerVat() { return customerVat; }
    public String getLicensePlate() { return licensePlate; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public double getEstimatedCost() { return estimatedCost; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(String actualEndDate) { this.actualEndDate = actualEndDate; }
    
    public abstract String getContractType();
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
            "\n Created On: " + creationDate.toLocalDate() +
            "\n Status: " + actualEndDate;
    }   
}