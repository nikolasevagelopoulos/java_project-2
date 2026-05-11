package managers;

import entities.vehicles.Vehicle;
import storage.StorableList;

public class VehicleManager {
    private StorableList<Vehicle> fleet;

    public VehicleManager(StorableList<Vehicle> loadedFleet) {
        this.fleet = loadedFleet;
    }

    // Προσθήκη νέου οχήματος 
    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    // Έλεγχος διαθεσιμότητας και επιστροφή κατάλληλου οχήματος 
    // (Στην πράξη θα πρέπει να ελέγχει και τα ενεργά συμβόλαια για να δει αν λείπει)
    public Vehicle findAvailableVehicle(String category, String type) {
        for (int i = 0; i < fleet.size(); i++) {
            Vehicle v = fleet.get(i);
            if (v.getCategory().equals(category)) {
                // Εδώ θα έμπαινε η λογική: "Είναι ενοικιασμένο αυτή τη στιγμή;"
                return v;
            }
        }
        return null; // Δεν βρέθηκε διαθέσιμο
    }

    public StorableList<Vehicle> getFleet() {
        return fleet;
    }
}