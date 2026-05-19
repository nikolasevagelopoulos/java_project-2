package storage;

import entities.vehicles.*;
import entities.user.*;
import contracts.*;
import requests.*;

import java.io.*;
import java.time.LocalDate;

public class StorageManager {

    public static final String USERS_FILE = "data/agents/agents.csv"; 
    public static final String FLEET_FILE = "data/vehicles/fleet.csv"; 
    public static final String CONTRACTS_FILE = "data/contracts/contracts.csv"; 
    
    public void saveListToFile(StorableList<? extends Storable> list, String filePath) {
        ensureDirectoryExists(filePath); 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < list.size(); i++) {
                Storable item = list.get(i);
                writer.write(item.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση στο αρχείο " + filePath + ": " + e.getMessage());
        }
    }

    public StorableList<Vehicle> loadFleet() {
        StorableList<Vehicle> fleetList = new StorableList<>();
        File file = new File(FLEET_FILE); 
        if (!file.exists()) return fleetList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                Vehicle vehicle = parts[0].trim().equalsIgnoreCase("PASSENGER") ? new PassengerCar() : new CommercialVan();
                vehicle.fromCSV(line);
                fleetList.add(vehicle);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του στόλου: " + e.getMessage());
        }
        return fleetList;
    }

    public StorableList<User> loadUsers() {
        StorableList<User> userList = new StorableList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return userList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0].trim().toUpperCase();
                
                User user = null;
                if (type.equals("INDIVIDUAL")) {
                    user = new Individual();
                } else if (type.equals("COMPANY")) {
                    user = new Company();
                } else if (type.equals("ADMIN")) {
                    user = new Admin();
                }
                
                if (user != null) {
                    user.fromCSV(line);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση χρηστών: " + e.getMessage());
        }
        return userList;
    }

    public StorableList<Contract> loadContracts() {
        StorableList<Contract> contractList = new StorableList<>();
        File file = new File(CONTRACTS_FILE);
        if (!file.exists()) return contractList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                Contract contract = parts[0].trim().equalsIgnoreCase("CAR") ? new CarRental() : new VanLease();
                contract.fromCSV(line);
                contractList.add(contract);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση συμβολαίων: " + e.getMessage());
        }
        return contractList;
    }

    public StorableList<Request> loadRequestsForDate(LocalDate date) {
        StorableList<Request> requestList = new StorableList<>();
        File file = new File("data/requests/pending/" + date.toString() + ".csv");
        if (!file.exists()) return requestList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                Request req = null;
                
                switch (parts[0].trim().toUpperCase()) {
                    case "BOOKING": req = new RentalBookingRequest(); break;
                    case "FINE":    req = new FinePaymentRequest(); break;
                    case "CANCEL":  req = new OtherRequests.RentalCancelationRequest(); break;
                    case "RETURN":  req = new OtherRequests.RentalReturnRequest(); break;
                    case "PAYMENT": req = new OtherRequests.CustomerPaymentRequest(); break;
                }
                
                if (req != null) {
                    req.fromCSV(line);
                    requestList.add(req);
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση αιτημάτων για " + date + ": " + e.getMessage());
        }
        return requestList;
    }

    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}
