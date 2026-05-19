package managers;

import contracts.CarRental;
import contracts.VanLease;
import contracts.Contract;
import entities.user.Customer;
import entities.user.Individual;
import entities.vehicles.Vehicle;
import finance.Charge;
import finance.Credit;
import requests.*;
import storage.StorableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RequestProcessor {
    private UserManager userManager;
    private VehicleManager vehicleManager;
    private ContractManager contractManager;
    private TransactionManager transactionManager;

    public RequestProcessor(UserManager um, VehicleManager vm, ContractManager cm, TransactionManager tm) {
        this.userManager = um;
        this.vehicleManager = vm;
        this.contractManager = cm;
        this.transactionManager = tm;
    }

    public void processDailyRequests(StorableList<Request> requests, LocalDate currentDate) {
        sortRequestsByPriority(requests);

        StorableList<Request> processedRequests = new StorableList<>();
        StorableList<Request> failedRequests = new StorableList<>();

        for (int i = 0; i < requests.size(); i++) {
            Request req = requests.get(i);
            boolean success = false;

            try {
                if (req instanceof RentalBookingRequest) {
                    success = processBooking((RentalBookingRequest) req, currentDate);
                } 
                else if (req instanceof OtherRequests.RentalReturnRequest) {
                    success = processReturn((OtherRequests.RentalReturnRequest) req, currentDate);
                } 
                else if (req instanceof OtherRequests.RentalCancelationRequest) {
                    success = processCancelation((OtherRequests.RentalCancelationRequest) req, currentDate);
                } 
                else if (req instanceof FinePaymentRequest) {
                    success = processFine((FinePaymentRequest) req);
                }
                else if (req instanceof OtherRequests.CustomerPaymentRequest) {
                    success = processPayment((OtherRequests.CustomerPaymentRequest) req);
                }

                if (success) {
                    processedRequests.add(req);
                } else {
                    failedRequests.add(req);
                }
            } catch (Exception e) {
                failedRequests.add(req);
            }
        }
        
        // Στο μέλλον: Εδώ θα καλείται ο StorageManager για να γράψει 
        // τις 2 λίστες (processedRequests & failedRequests) στα αντίστοιχα CSV.
    }

    // --- 1. ΑΙΤΗΜΑ ΚΡΑΤΗΣΗΣ (Booking) ---
    private boolean processBooking(RentalBookingRequest req, LocalDate currentDate) {
        // Αποτυχία αν η ημερομηνία έναρξης είναι στο παρελθόν (όπως στο ReadMe.pdf 5 Απριλίου)
        if (req.getRentalStartDate().toLocalDate().isBefore(currentDate)) {
            return false; 
        }

        Customer customer = (Customer) userManager.getUserByVat(req.getCustomerVat());
        if (customer == null) return false;

        String vehicleType = (customer instanceof Individual) ? "PASSENGER" : "VAN";
        Vehicle vehicle = vehicleManager.findAvailableVehicle(req.getVehicleCategory(), vehicleType, contractManager);
        
        if (vehicle == null) return false;

        Contract contract;
        if (vehicleType.equals("PASSENGER")) {
            contract = new CarRental(req.getReferenceId(), customer.getVat(), vehicle.getLicensePlate(), 
                                     req.getRentalStartDate(), req.getRentalEndDate(), vehicle.getPrice());
            contractManager.createContract(contract);
            
            // Xρέωση αμέσως στην κράτηση για CarRental
            Charge charge = new Charge("TX_" + req.getRequestId(), req.getReferenceId(), 
                                       contract.getEstimatedCost(), "Rental Charge", Charge.ChargeType.RENTAL);
            transactionManager.executeTransaction(customer, charge);
        } else {
            // Για VanLeasing: Ημερομηνίες στρογγυλοποιούνται σε μήνες
            LocalDateTime start = req.getRentalStartDate().withDayOfMonth(1);
            LocalDateTime end = start.plusMonths(req.getDuration() / 30); // Απλοϊκή μετατροπή ημερών σε μήνες
            
            contract = new VanLease(req.getReferenceId(), customer.getVat(), vehicle.getLicensePlate(), 
                                    start, end, vehicle.getPrice());
            contractManager.createContract(contract);
            
            // ΔΕΝ χρεώνεται τώρα. Θα χρεώνεται 1η κάθε μήνα μέσω SystemTimer/CLI.
        }
        return true;
    }

 // --- 2. ΑΙΤΗΜΑ ΕΠΙΣΤΡΟΦΗΣ (Return) ---
    private boolean processReturn(OtherRequests.RentalReturnRequest req, LocalDate currentDate) {
        String targetContractId = req.getReferenceId(); // <-- Διορθώθηκε
        Contract contract = null;
        StorableList<Contract> allContracts = contractManager.getAllContracts();
        
        // Σωστή αναζήτηση με βάση το Contract ID (Reference ID)
        for (int i = 0; i < allContracts.size(); i++) {
            Contract c = allContracts.get(i);
            if (c.getContractId().equals(targetContractId) && c.getActualEndDate().equals("PENDING")) {
                contract = c;
                break;
            }
        }
        
        if (contract == null || !contract.getCustomerVat().equals(req.getCustomerVat())) return false; 

        contract.setActualEndDate(currentDate.toString());
        
        if (contract instanceof CarRental) {
            long actualDays = ChronoUnit.DAYS.between(contract.getStartDate().toLocalDate(), currentDate);
            if (actualDays <= 0) actualDays = 1;
            
            double finalCost = actualDays * contract.getRate();
            Customer customer = (Customer) userManager.getUserByVat(contract.getCustomerVat());

            if (finalCost > contract.getEstimatedCost()) {
                double extraCost = (finalCost - contract.getEstimatedCost()) * 1.5;
                Charge overdue = new Charge("OVD_" + req.getRequestId(), contract.getContractId(), extraCost, "Overdue Charge", Charge.ChargeType.OVERDUE);
                transactionManager.executeTransaction(customer, overdue);
            } 
            else if (finalCost < contract.getEstimatedCost()) {
                double refundAmount = (contract.getEstimatedCost() - finalCost) * 0.8;
                Credit refund = new Credit("REF_" + req.getRequestId(), contract.getContractId(), refundAmount, "Early Return Refund", Credit.CreditType.CONTRACT_REFUND);
                transactionManager.executeTransaction(customer, refund);
            }
        }
        return true;
    }

    // --- 3. ΑΙΤΗΜΑ ΑΚΥΡΩΣΗΣ (Cancellation) ---
    private boolean processCancelation(OtherRequests.RentalCancelationRequest req, LocalDate currentDate) {
        String targetContractId = req.getReferenceId(); // <-- Διορθώθηκε
        Contract contract = null;
        StorableList<Contract> allContracts = contractManager.getAllContracts();
        
        // Σωστή αναζήτηση με βάση το Contract ID
        for (int i = 0; i < allContracts.size(); i++) {
            Contract c = allContracts.get(i);
            if (c.getContractId().equals(targetContractId) && c.getActualEndDate().equals("PENDING")) {
                contract = c;
                break;
            }
        }

        if (contract == null) return false; 
        
        // Ακύρωση επιτρέπεται μόνο αν δεν έχει ξεκινήσει
        if (!currentDate.isBefore(contract.getStartDate().toLocalDate())) return false;

        contract.setActualEndDate("CANCELED_" + currentDate);
        Customer customer = (Customer) userManager.getUserByVat(contract.getCustomerVat());

        double refundMultiplier = (contract instanceof CarRental) ? 0.8 : 0.5;
        double refundAmount = contract.getEstimatedCost() * refundMultiplier;

        Credit refund = new Credit("REF_" + req.getRequestId(), contract.getContractId(), refundAmount, "Cancellation Refund", Credit.CreditType.CONTRACT_REFUND);
        transactionManager.executeTransaction(customer, refund);
        
        return true;
    }

    // --- 4. ΑΙΤΗΜΑ ΠΛΗΡΩΜΗΣ ΠΡΟΣΤΙΜΟΥ (Fine) ---
    private boolean processFine(FinePaymentRequest req) {
        // Εύρεση του συμβολαίου που ήταν ενεργό την ημέρα της κλήσης (noticeDate)
        Contract targetContract = null;
        StorableList<Contract> allContracts = contractManager.getAllContracts();
        
        for (int i = 0; i < allContracts.size(); i++) {
            Contract c = allContracts.get(i);
            if (c.getLicensePlate().equals(req.getVehicleLicensePlate())) {
                LocalDate start = c.getStartDate().toLocalDate();
                LocalDate end = c.getEndDate().toLocalDate();
                LocalDate notice = req.getNoticeDate().toLocalDate();
                
                if ((notice.isEqual(start) || notice.isAfter(start)) && (notice.isEqual(end) || notice.isBefore(end))) {
                    targetContract = c;
                    break;
                }
            }
        }

        if (targetContract == null) return false;

        Customer customer = (Customer) userManager.getUserByVat(targetContract.getCustomerVat());
        Charge fineCharge = new Charge("FINE_" + req.getRequestId(), req.getReferenceId(), req.getAmount(), "KOK Fine", Charge.ChargeType.FINE);
        transactionManager.executeTransaction(customer, fineCharge);
        return true;
    }

    // --- 5. ΑΙΤΗΜΑ ΠΛΗΡΩΜΗΣ ΠΕΛΑΤΗ (Customer Payment) ---
    private boolean processPayment(OtherRequests.CustomerPaymentRequest req) {
        Customer customer = (Customer) userManager.getUserByVat(req.getCustomerVat());
        if (customer == null) return false;

        // Υποθέτουμε ότι η κλάση έχει πλέον προστεθεί το Amount
        Credit payment = new Credit("PAY_" + req.getRequestId(), null, req.getAmount(), "Wallet Top-Up", Credit.CreditType.CUSTOMER_PAYMENT);
        transactionManager.executeTransaction(customer, payment);
        return true;
    }

    private void sortRequestsByPriority(StorableList<Request> requests) {
        for (int i = 0; i < requests.size() - 1; i++) {
            for (int j = 0; j < requests.size() - i - 1; j++) {
                Request r1 = requests.get(j);
                Request r2 = requests.get(j + 1);
                boolean shouldSwap = false;

                if (r1 instanceof RentalBookingRequest && r2 instanceof RentalBookingRequest) {
                    RentalBookingRequest b1 = (RentalBookingRequest) r1;
                    RentalBookingRequest b2 = (RentalBookingRequest) r2;
                    if (b1.getDuration() < b2.getDuration()) shouldSwap = true;
                    else if (b1.getDuration() == b2.getDuration() && b1.getTimestamp().compareTo(b2.getTimestamp()) > 0) shouldSwap = true;
                } else {
                    if (r1.getTimestamp().compareTo(r2.getTimestamp()) > 0) shouldSwap = true;
                }

                if (shouldSwap) {
                    requests.set(j, r2);
                    requests.set(j + 1, r1);
                }
            }
        }
    }
}