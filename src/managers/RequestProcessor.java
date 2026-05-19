package managers;

import contracts.CarRental;
import contracts.Contract;
import entities.user.Customer;
import entities.vehicles.PassengerCar;
import entities.vehicles.Vehicle;
import finance.Transaction;
import requests.FinePaymentRequest;
import requests.RentalBookingRequest;
import requests.Request;
import storage.StorableList;

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

    public void processDailyRequests(StorableList<Request> requests, String currentDate) {
        sortRequestsByPriority(requests);

        for (int i = 0; i < requests.size(); i++) {
            Request req = requests.get(i);
            String type = req.getType();

            if (type.equals("BOOKING")) {
                RentalBookingRequest bookingReq = (RentalBookingRequest) req;
                Customer customer = (Customer) userManager.getUserByVat(bookingReq.getVat());
                
                if (customer != null) {
                    // Αναζήτηση οχήματος. Υποθέτουμε ότι ζητάει PASSENGER για το παράδειγμα.
                    Vehicle vehicle = vehicleManager.findAvailableVehicle(bookingReq.getVehicleCategory(), "PASSENGER", contractManager);
                    
                    if (vehicle != null) {
                        double estimatedCost = vehicle.getPrice() * bookingReq.getDuration();
                        Contract contract = new CarRental(
                            bookingReq.getReferenceId(), 
                            customer.getVat(), 
                            vehicle.getLicensePlate(), 
                            currentDate, 
                            "ESTIMATED_END", // Απλοποίηση για τις ημερομηνίες
                            estimatedCost
                        );
                        
                        contractManager.createContract(contract);
                        
                        Transaction charge = new Transaction("CHARGE", estimatedCost, "Rental Charge for " + vehicle.getLicensePlate());
                        transactionManager.executeTransaction(customer, charge, currentDate);
                    }
                }
            } 
            else if (type.equals("RETURN")) {
                // Υποθέτουμε ότι το referenceId του αιτήματος ταιριάζει με το συμβόλαιο
                Contract contract = contractManager.completeContract(req.getReferenceId(), currentDate);
                if (contract != null) {
                    // Εδώ θα έμπαινε η λογική ελέγχου ημερομηνιών για εκπρόθεσμη επιστροφή
                    // π.χ. υπολογισμός OverdueCharge (20% παραπάνω για passenger)
                }
            } 
            else if (type.equals("CANCEL")) {
                contractManager.cancelContract(req.getReferenceId(), currentDate);
            }
            else if (type.equals("FINE")) {
                FinePaymentRequest fineReq = (FinePaymentRequest) req;
                Contract activeContract = contractManager.findActiveContractByPlate(fineReq.getLicensePlate());
                
                if (activeContract != null) {
                    Customer customer = (Customer) userManager.getUserByVat(activeContract.getCustomerVat());
                    Transaction fineCharge = new Transaction("CHARGE", fineReq.getAmount(), "KOK Fine");
                    transactionManager.executeTransaction(customer, fineCharge, currentDate);
                }
            }
        }
    }

    private void sortRequestsByPriority(StorableList<Request> requests) {
        // Bubble sort: Φθίνουσα σειρά διάρκειας για τα BOOKING, 
        // αύξουσα σειρά timestamp για τα υπόλοιπα.
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
                        // Αν έχουν ίδια διάρκεια, συγκρίνουμε timestamp
                        if (b1.getTimestamp().compareTo(b2.getTimestamp()) > 0) {
                            shouldSwap = true;
                        }
                    }
                } else {
                    // Αν δεν είναι Booking, απλή σύγκριση timestamp
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