package managers;

import contracts.CarRental;
import contracts.VanLease;
import contracts.Contract;
import entities.user.Customer;
import entities.user.Individual;
import entities.vehicles.Vehicle;
import finance.Transaction;
import finance.Charge;
import requests.FinePaymentRequest;
import requests.RentalBookingRequest;
import requests.Request;
import storage.StorableList;
import java.time.LocalDateTime;

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

    public void processDailyRequests(StorableList<Request> requests, LocalDateTime currentDate) {
        sortRequestsByPriority(requests);

        for (int i = 0; i < requests.size(); i++) {
            Request req = requests.get(i);
            String type = req.getType();

            if (type.equals("BOOKING")) {
                RentalBookingRequest bookingReq = (RentalBookingRequest) req;
                Customer customer = (Customer) userManager.getUserByVat(bookingReq.getCustomerVat());
                
                if (customer != null) {
                    String vehicleType = (customer instanceof Individual) ? "PASSENGER" : "VAN"; [cite: 26, 27]
                    Vehicle vehicle = vehicleManager.findAvailableVehicle(bookingReq.getVehicleCategory(), vehicleType, contractManager);
                    
                    if (vehicle != null) {
                        double estimatedCost = vehicle.getPrice() * bookingReq.getDuration();
                        Contract contract;
                        
                        if (vehicleType.equals("PASSENGER")) {
                            contract = new CarRental(
                                bookingReq.getReferenceId(), 
                                customer.getVat(), 
                                vehicle.getLicensePlate(), 
                                currentDate, 
                                bookingReq.getRentalEndDate(), 
                                vehicle.getPrice()
                            );
                        } else {
                            contract = new VanLease(
                                bookingReq.getReferenceId(), 
                                customer.getVat(), 
                                vehicle.getLicensePlate(), 
                                currentDate, 
                                bookingReq.getRentalEndDate(), 
                                vehicle.getPrice()
                            );
                        }
                        
                        contractManager.createContract(contract);
                        
                        Transaction charge = new Charge(
                            "TX_" + bookingReq.getRequestId(),
                            bookingReq.getReferenceId(),
                            estimatedCost,
                            "Rental Charge for " + vehicle.getLicensePlate(),
                            Charge.ChargeType.RENTAL
                        );
                        transactionManager.executeTransaction(customer, charge);
                    }
                }
            } 
            else if (type.equals("RETURN")) {
                Contract contract = contractManager.completeContract(req.getReferenceId(), currentDate.toString());
                if (contract != null) {
                    // Overdue computation hook [cite: 80]
                }
            } 
            else if (type.equals("CANCEL")) {
                contractManager.cancelContract(req.getReferenceId(), currentDate.toString());
            }
            else if (type.equals("FINE")) {
                FinePaymentRequest fineReq = (FinePaymentRequest) req;
                Contract activeContract = contractManager.findActiveContractByPlate(fineReq.getVehicleLicensePlate());
                
                if (activeContract != null) {
                    Customer customer = (Customer) userManager.getUserByVat(activeContract.getCustomerVat());
                    Transaction fineCharge = new Charge(
                        "TX_" + fineReq.getRequestId(),
                        fineReq.getReferenceId(),
                        fineReq.getAmount(),
                        "KOK Fine",
                        Charge.ChargeType.FINE
                    );
                    transactionManager.executeTransaction(customer, fineCharge);
                }
            }
        }
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
                    
                    if (b1.getDuration() < b2.getDuration()) {
                        shouldSwap = true;
                    } else if (b1.getDuration() == b2.getDuration()) {
                        if (b1.getTimestamp().compareTo(b2.getTimestamp()) > 0) {
                            shouldSwap = true;
                        }
                    }
                } else {
                    if (r1.getTimestamp().compareTo(r2.getTimestamp()) > 0) {
                        shouldSwap = true;
                    }
                }

                if (shouldSwap) {
                    requests.set(j, r2);
                    requests.set(j + 1, r1);
                }
            }
        }
    }
}
