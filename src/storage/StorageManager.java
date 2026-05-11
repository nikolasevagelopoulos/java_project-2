package storage;

import entities.vehicles.CommercialVan;
import entities.vehicles.PassengerCar;
import entities.vehicles.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StorageManager {

    // Καθορισμός των σταθερών μονοπατιών όπως ζητάει η εκφώνηση
    private static final String USERS_FILE = "data/agents/agents.csv"; // [cite: 100]
    private static final String FLEET_FILE = "data/vehicles/fleet.csv"; // [cite: 101]
    private static final String CONTRACTS_FILE = "data/contracts/contracts.csv"; // [cite: 102]
    
    /**
     * Γενική μέθοδος αποθήκευσης (Save).
     * Δέχεται οποιαδήποτε StorableList και την αποθηκεύει στο path που θα της δώσεις.
     */
    public void saveListToFile(StorableList<? extends Storable> list, String filePath) {
        ensureDirectoryExists(filePath); // Δημιουργεί τους φακέλους αν δεν υπάρχουν
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < list.size(); i++) {
                Storable item = list.get(i);
                writer.write(item.toCSV());
                writer.newLine();
            }
            System.out.println("Επιτυχής αποθήκευση στο: " + filePath);
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση στο αρχείο " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Ειδική μέθοδος ανάκτησης (Load) για τα Οχήματα (Fleet).
     * Διαβάζει το fleet.csv και αναγνωρίζει αν το όχημα είναι PassengerCar ή CommercialVan.
     */
    public StorableList<Vehicle> loadFleet() {
        StorableList<Vehicle> fleetList = new StorableList<>();
        File file = new File(FLEET_FILE); // [cite: 101]

        if (!file.exists()) {
            System.out.println("Το αρχείο στόλου δεν βρέθηκε. Ξεκινάμε με άδειο στόλο.");
            return fleetList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Αγνόησε τις κενές γραμμές
                
                String[] parts = line.split(",");
                String type = parts[0]; // Βλέπουμε το πρώτο στοιχείο για να καταλάβουμε τον τύπο
                
                Vehicle vehicle = null;
                if (type.equals("PASSENGER")) {
                    vehicle = new PassengerCar();
                } else if (type.equals("VAN")) {
                    vehicle = new CommercialVan();
                }

                if (vehicle != null) {
                    vehicle.fromCSV(line); // Γεμίζει το αντικείμενο με τα δεδομένα της γραμμής
                    fleetList.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου στόλου: " + e.getMessage());
        }

        return fleetList;
    }

    /**
     * Βοηθητική μέθοδος που ελέγχει αν υπάρχουν οι φάκελοι (π.χ. data/vehicles/)
     * και αν όχι, τους δημιουργεί αυτόματα πριν πάει να γράψει το αρχείο.
     */
    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Δημιουργεί όλο το δέντρο φακέλων 
        }
    }
}