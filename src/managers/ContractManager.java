package managers;

import contracts.Contract;
import storage.StorableList;

public class ContractManager {
    private StorableList<Contract> contracts;

    public ContractManager(StorableList<Contract> loadedContracts) {
        this.contracts = loadedContracts;
    }

    // Δημιουργία 
    public void createContract(Contract contract) {
        contracts.add(contract);
        System.out.println("Το συμβόλαιο δημιουργήθηκε: " + contract.getReferenceId());
    }

    // Ακύρωση (Επιτρέπεται ως και μια ημέρα πριν την έναρξη) [cite: 75, 91]
    public boolean cancelContract(String referenceId, String currentDate) {
        // TODO: Εύρεση συμβολαίου, έλεγχος ημερομηνίας, αλλαγή κατάστασης σε "CANCELED"
        return true;
    }

    // Ολοκλήρωση / Επιστροφή 
    public void completeContract(String referenceId, String returnDate) {
        // TODO: Ενημέρωση της πραγματικής ημερομηνίας επιστροφής [cite: 77]
    }
}