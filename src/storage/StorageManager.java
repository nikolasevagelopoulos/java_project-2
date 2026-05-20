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
                writer.write(list.get(i).toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
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
                Vehicle vehicle = line.contains("PassengerCar") ? new PassengerCar() : new CommercialVan();
                vehicle.fromCSV(line);
                fleetList.add(vehicle);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα στόλου: " + e.getMessage());
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
                User user = null;
                if (line.contains("type:Admin")) user = new Admin();
                else if (line.contains("type:Individual")) user = new Individual();
                else if (line.contains("type:Company")) user = new Company();
                
                if (user != null) {
                    user.fromCSV(line);
                    
                    // FORCE INITIAL BALANCE: Αν είναι πελάτης, του φορτώνουμε με το ζόρι 5000 ευρώ 
                    // για να μην επηρεάζεται από το "balance:0.0" του αρχείου!
                    if (user instanceof Customer) {
                        Customer c = (Customer) user;
                        if (c.getWallet() != null) {
                            c.getWallet().balance = 5000.0;
                        }
                    }
                    
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα χρηστών: " + e.getMessage());
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
                Contract contract = line.contains("type:CAR") ? new CarRental() : new VanLease();
                contract.fromCSV(line);
                contractList.add(contract);
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα συμβολαίων: " + e.getMessage());
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
                Request req = null;
                if (line.contains("type:BOOKING")) req = new RentalBookingRequest();
                else if (line.contains("type:FINE")) req = new FinePaymentRequest();
                else if (line.contains("type:CANCEL")) req = new OtherRequests.RentalCancelationRequest();
                else if (line.contains("type:RETURN")) req = new OtherRequests.RentalReturnRequest();
                else if (line.contains("type:PAYMENT")) req = new OtherRequests.CustomerPaymentRequest();
                
                if (req != null) {
                    req.fromCSV(line);
                    requestList.add(req);
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα αιτημάτων: " + e.getMessage());
        }
        return requestList;
    }

    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
    }
}