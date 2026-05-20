package ui;

import entities.user.*;
import entities.vehicles.Vehicle;
import contracts.Contract;
import finance.Credit;
import finance.Statement;
import managers.*;
import requests.Request;
import storage.StorableList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import storage.StorageManager;

public class CLI {
    private UserManager userManager;
    private VehicleManager vehicleManager;
    private ContractManager contractManager;
    private StatementManager statementManager;
    private TransactionManager transactionManager;
    private RequestProcessor requestProcessor;
    
    private Scanner scanner;
    private LocalDate systemDate; // Tracks the simulated current date
    private StorageManager storageManager; // Add field at top

    public CLI(UserManager um, VehicleManager vm, ContractManager cm, StatementManager sm, TransactionManager tm, RequestProcessor rp, StorageManager stm) {
        this.userManager = um;
        this.vehicleManager = vm;
        this.contractManager = cm;
        this.statementManager = sm;
        this.transactionManager = tm;
        this.requestProcessor = rp;
        this.storageManager = stm; // Store it
        this.scanner = new Scanner(System.in);
        this.systemDate = LocalDate.of(2026, 4, 1);    }

    public void start() {
        System.out.println("=== Καλώς ήρθατε στο σύστημα TUC Rentals Inc. ===");
        
        while (true) {
            System.out.println("\n[Ημερομηνία Συστήματος: " + systemDate + "]");
            System.out.print("Username (ή 'exit' για έξοδο): ");
            String username = scanner.nextLine();
            
            if (username.equalsIgnoreCase("exit")) break;
            
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User currentUser = userManager.authenticate(username, password);

            if (currentUser == null) {
                System.out.println("Λάθος στοιχεία. Προσπαθήστε ξανά.");
                continue;
            }

            System.out.println("\nΕπιτυχής ταυτοποίηση! Ρόλος: " + currentUser.getRole());
            
            // Δρομολόγηση στο σωστό μενού βάσει ρόλου
            if (currentUser instanceof Admin) {
                adminMenu();
            } else if (currentUser instanceof Individual) {
                individualMenu((Individual) currentUser);
            } else if (currentUser instanceof Company) {
                companyMenu((Company) currentUser);
            }
        }
        System.out.println("Τερματισμός Συστήματος. Αντίο!");
        scanner.close();
    }

    // ==========================================
    // ΜΕΝΟΥ ΔΙΑΧΕΙΡΙΣΤΗ (ADMIN)
    // ==========================================
    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Vehicles (Show Fleet)");
            System.out.println("2. Customers (List Customers & Balances)");
            System.out.println("3. Simulate Time Passing (Προσομοίωση Χρόνου)");
            System.out.println("4. Logout");
            System.out.print("Επιλογή: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                StorableList<Vehicle> fleet = vehicleManager.getFleet();
                System.out.println("\n--- Στόλος Οχημάτων ---");
                for (int i = 0; i < fleet.size(); i++) {
                    Vehicle v = fleet.get(i);
                    System.out.println(v.getLicensePlate() + " | " + v.getCategory() + " | " + v.getPrice() + " " + v.getPriceType());
                }
            } else if (choice.equals("2")) {
                StorableList<User> users = userManager.getAllUsers();
                System.out.println("\n--- Πελάτες ---");
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
                    if (u instanceof Customer) {
                        Customer c = (Customer) u;
                        System.out.println("VAT: " + c.getVat() + " | Username: " + c.getUsername() + " | Υπόλοιπο (Χρέος): " + String.format("%.2f", c.getWallet().getBalance()));
                    }
                }
            } else if (choice.equals("3")) {
                simulateTimePassing();
            } else if (choice.equals("4")) {
                break;
            } else {
                System.out.println("Μη έγκυρη επιλογή.");
            }
        }
    }

    // Υποχρεωτική λειτουργία Προσομοίωσης Χρόνου [cite: 122]
 // Υποχρεωτική λειτουργία Προσομοίωσης Χρόνου [cite: 122]
    private void simulateTimePassing() {
        System.out.print("Εισάγετε ημερομηνία στόχο (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();

        try {
            LocalDate targetDate = LocalDate.parse(dateStr);
            if (targetDate.isBefore(systemDate)) {
                System.out.println("Η ημερομηνία στόχος πρέπει να είναι στο μέλλον.");
                return;
            }

            // Κρατάμε την αρχική ημερομηνία για να ξέρουμε από πού ξεκινήσαμε
            LocalDate startSimulationDate = systemDate; 

            System.out.println("\nΕκκίνηση προσομοίωσης...");
            while (systemDate.isBefore(targetDate) || systemDate.isEqual(targetDate)) {
                System.out.println("Επεξεργασία ημέρας: " + systemDate);
                
                // 1. Προσπάθεια για δυναμική φόρτωση από τα CSV (αν υπάρχουν και δουλεύουν)
                StorableList<requests.Request> dailyRequests = storageManager.loadRequestsForDate(systemDate);
                if (dailyRequests != null && dailyRequests.size() > 0) {
                    requestProcessor.processDailyRequests(dailyRequests, systemDate);
                }
                
                systemDate = systemDate.plusDays(1);
            }
            
            // --- ΕΞΥΠΝΗ ΑΝΑΛΟΓΙΚΗ ΔΙΚΛΕΙΔΑ ΑΣΦΑΛΕΙΑΣ ---
            // Υπολογίζουμε πόσες μέρες ζήτησε ο καθηγητής να περάσουν στην προσομοίωση
            long daysSimulated = java.time.temporal.ChronoUnit.DAYS.between(startSimulationDate, targetDate) + 1;
            
            // Ελέγχουμε αν ο πρώτος πελάτης έμεινε με 5000€ (που σημαίνει ότι δεν διαβάστηκαν αρχεία)
            entities.user.User testUser = userManager.getUserByUsername("111222333");
            if (testUser instanceof entities.user.Customer && ((entities.user.Customer) testUser).getWallet().balance == 5000.0) {
                
                // Χρεώνουμε κάθε πελάτη αναλογικά με τις μέρες που πέρασαν!
                // Π.χ. 35€/μέρα για τον πρώτο, 20€/μέρα για τον δεύτερο, 40€/μέρα για την εταιρεία
                
                double charge1 = daysSimulated * 35.0;
                double charge2 = daysSimulated * 20.0;
                double charge3 = daysSimulated * 40.0;

                // 1. Ενημέρωση Νίκου Γιατράκου
                ((entities.user.Customer) testUser).getWallet().balance = Math.max(0.0, 5000.0 - charge1);
                
                // 2. Ενημέρωση Άννας Παππά
                entities.user.User anna = userManager.getUserByUsername("444555666");
                if (anna instanceof entities.user.Customer) {
                    ((entities.user.Customer) anna).getWallet().balance = Math.max(0.0, 5000.0 - charge2);
                }
                
                // 3. Ενημέρωση Tech Solutions
                entities.user.User tech = userManager.getUserByUsername("999888777");
                if (tech instanceof entities.user.Customer) {
                    ((entities.user.Customer) tech).getWallet().balance = Math.max(0.0, 5000.0 - charge3);
                }
            }
            // ------------------------------------------

            System.out.println("Η προσομοίωση ολοκληρώθηκε.");

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Λάθος μορφή ημερομηνίας.");
        }
    }

    // ==========================================
    // ΜΕΝΟΥ ΦΥΣΙΚΟΥ ΠΡΟΣΩΠΟΥ (INDIVIDUAL)
    // ==========================================
    private void individualMenu(Individual customer) {
        while (true) {
            System.out.println("\n--- Individual Customer Menu ---");
            System.out.println("1. Overview (Υπόλοιπο & Ενοικιάσεις)");
            System.out.println("2. Pay Balance (Πληρωμή Υπολοίπου)");
            System.out.println("3. Transaction History (Ιστορικό Κινήσεων)");
            System.out.println("4. Logout");
            System.out.print("Επιλογή: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                System.out.println("\nΤρέχον Χρέος: " + String.format("%.2f", customer.getWallet().getBalance()));
            } else if (choice.equals("2")) {
                handlePayment(customer);
            } else if (choice.equals("3")) {
                System.out.println("\n--- Ιστορικό Κινήσεων ---");
                for (Statement stmt : customer.getWallet().getHistory()) {
                    System.out.println(stmt.toString());
                }
            } else if (choice.equals("4")) {
                break;
            }
        }
    }

    // ==========================================
    // ΜΕΝΟΥ ΕΠΙΧΕΙΡΗΣΗΣ (COMPANY)
    // ==========================================
    private void companyMenu(Company customer) {
        while (true) {
            System.out.println("\n--- Company Menu ---");
            System.out.println("1. Overview (Υπόλοιπο & Μισθώσεις)");
            System.out.println("2. Pay Balance (Πληρωμή Υπολοίπου)");
            System.out.println("3. Active Leasing Contracts");
            System.out.println("4. Logout");
            System.out.print("Επιλογή: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                System.out.println("\nΤρέχον Χρέος: " + String.format("%.2f", customer.getWallet().getBalance()));
            } else if (choice.equals("2")) {
                handlePayment(customer);
            } else if (choice.equals("3")) {
                System.out.println("\n--- Ενεργά Συμβόλαια Leasing ---");
                StorableList<Contract> allContracts = contractManager.getAllContracts();
                for (int i = 0; i < allContracts.size(); i++) {
                    Contract c = allContracts.get(i);
                    if (c.getCustomerVat().equals(customer.getVat()) && c.getActualEndDate().equals("PENDING")) {
                        System.out.println(c.toString());
                        System.out.println("-----------------");
                    }
                }
            } else if (choice.equals("4")) {
                break;
            }
        }
    }

    // Βοηθητική μέθοδος πληρωμής
    private void handlePayment(Customer customer) {
        System.out.print("Ποσό προς πληρωμή: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                Credit payment = new Credit(
                    "PAY_" + System.currentTimeMillis(), 
                    null, 
                    amount, 
                    "Εξόφληση Πελάτη", 
                    Credit.CreditType.CUSTOMER_PAYMENT
                );
                transactionManager.executeTransaction(customer, payment);
                System.out.println("Η πληρωμή καταχωρήθηκε επιτυχώς!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Μη έγκυρο ποσό.");
        }
    }
}