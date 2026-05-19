package managers;

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

    // Η καρδιά της "Προσομοίωσης Χρόνου"
    public void processDailyRequests(StorableList<Request> dailyRequests) {
        // 1. Ταξινόμηση των αιτημάτων μίσθωσης: 
        // Πρώτα βάσει διάρκειας (φθίνουσα) και μετά βάσει χρονοσήμανσης (αύξουσα)[cite: 83].
        sortRequestsByPriority(dailyRequests);

        // 2. Δρομολόγηση 
        for (int i = 0; i < dailyRequests.size(); i++) {
            Request req = dailyRequests.get(i);
            
            if (req.getType().equals("BOOKING")) {
                // TODO: Βρες όχημα -> Φτιάξε Συμβόλαιο -> Χρέωσε πελάτη
            } else if (req.getType().equals("RETURN")) {
                // TODO: Κλείσε Συμβόλαιο -> Έλεγξε για Overdue Charges [cite: 44, 80] -> Αποδέσμευσε όχημα
            }
            // κ.ο.κ για τα υπόλοιπα [cite: 58-61]
        }
    }

    private void sortRequestsByPriority(StorableList<Request> requests) {
        // TODO: Υλοποίηση αλγορίθμου ταξινόμησης (π.χ. Bubble Sort) βάσει των κανόνων[cite: 83].
    }
}