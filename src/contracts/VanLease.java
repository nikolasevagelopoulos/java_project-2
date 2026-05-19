package contracts;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class VanLease extends Contract {
    private double monthlyRate;
    
    public VanLease(String contractId, String customerVat, String licensePlate, LocalDateTime startDate, LocalDateTime endDate, double monthlyRate) {
        super(contractId, customerVat, licensePlate, startDate, endDate, calculateEstimatedCost(startDate, endDate, monthlyRate));
        this.monthlyRate = monthlyRate;
    }

    public VanLease() {
        super(null, null, null, null, null, 0.0);
    }

    private static double calculateEstimatedCost(LocalDateTime start, LocalDateTime end, double rate) {
        long months = ChronoUnit.MONTHS.between(start, end);
        if (months <= 0) months = 1;
        return months * rate;
    }

    @Override
    public String getContractType() { return "Van Lease"; }

    @Override
    public double getRate() { return monthlyRate; }

    @Override
    public double getDailyRate() { return monthlyRate / 30.0; }

    @Override
    public String toCSV() {
        return "VAN," + contractId + "," + customerVat + "," + licensePlate + "," + 
               startDate + "," + endDate + "," + estimatedCost + "," + actualEndDate + "," + monthlyRate;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        this.contractId = parts[1];
        this.customerVat = parts[2];
        this.licensePlate = parts[3];
        this.startDate = LocalDateTime.parse(parts[4]);
        this.endDate = LocalDateTime.parse(parts[5]);
        this.estimatedCost = Double.parseDouble(parts[6]);
        this.actualEndDate = parts[7];
        this.monthlyRate = Double.parseDouble(parts[8]);
    }
    
    @Override
    public String toString() {
        return super.toString() + "\n Monthly Rate: " + String.format("%.2f", monthlyRate);
    }
}