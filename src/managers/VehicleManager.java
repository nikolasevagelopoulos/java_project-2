package managers;

import entities.vehicles.CommercialVan;
import entities.vehicles.PassengerCar;
import entities.vehicles.Vehicle;
import storage.StorableList;

public class VehicleManager {
    private StorableList<Vehicle> fleet;

    public VehicleManager(StorableList<Vehicle> loadedFleet) {
        this.fleet = loadedFleet;
    }

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public Vehicle findAvailableVehicle(String category, String type, ContractManager contractManager) {
        for (int i = 0; i < fleet.size(); i++) {
            Vehicle v = fleet.get(i);
            
            if (v.getCategory().equals(category)) {
                // Έλεγχος τύπου οχήματος
                boolean isCorrectType = (type.equals("PASSENGER") && v instanceof PassengerCar) || 
                                        (type.equals("VAN") && v instanceof CommercialVan);
                
                if (isCorrectType) {
                    // Έλεγχος διαθεσιμότητας μέσω του ContractManager
                    if (contractManager.isVehicleAvailable(v.getLicensePlate())) {
                        return v;
                    }
                }
            }
        }
        return null; // Κανένα διαθέσιμο όχημα
    }

    public StorableList<Vehicle> getFleet() {
        return fleet;
    }
}