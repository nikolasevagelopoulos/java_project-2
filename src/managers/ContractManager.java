package managers;

import contracts.Contract;
import storage.StorableList;

public class ContractManager {
    private StorableList<Contract> contracts;

    public ContractManager(StorableList<Contract> loadedContracts) {
        this.contracts = loadedContracts;
    }

    public void createContract(Contract contract) {
        contracts.add(contract);
    }

    public boolean isVehicleAvailable(String plate) {
        for (int i = 0; i < contracts.size(); i++) {
            Contract c = contracts.get(i);
            if (c.getVehiclePlate().equals(plate) && c.getActualEndDate().equals("PENDING")) {
                return false; // Το όχημα είναι αυτή τη στιγμή νοικιασμένο
            }
        }
        return true;
    }

    public Contract findActiveContractByPlate(String plate) {
        for (int i = 0; i < contracts.size(); i++) {
            Contract c = contracts.get(i);
            if (c.getVehiclePlate().equals(plate) && c.getActualEndDate().equals("PENDING")) {
                return c;
            }
        }
        return null;
    }

    public boolean cancelContract(String referenceId, String currentDate) {
        for (int i = 0; i < contracts.size(); i++) {
            Contract c = contracts.get(i);
            if (c.getReferenceId().equals(referenceId) && c.getActualEndDate().equals("PENDING")) {
                c.setActualEndDate("CANCELED_" + currentDate);
                return true;
            }
        }
        return false;
    }

    public Contract completeContract(String referenceId, String actualReturnDate) {
        for (int i = 0; i < contracts.size(); i++) {
            Contract c = contracts.get(i);
            if (c.getReferenceId().equals(referenceId) && c.getActualEndDate().equals("PENDING")) {
                c.setActualEndDate(actualReturnDate);
                return c;
            }
        }
        return null;
    }

    public StorableList<Contract> getAllContracts() {
        return contracts;
    }
}