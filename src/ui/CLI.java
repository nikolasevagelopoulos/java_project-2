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
            
            System.out.println("\nΕκκίνηση προσομοίωσης...");
            while (systemDate.isBefore(targetDate) || systemDate.isEqual(targetDate)) {
                System.out.println("Επεξεργασία ημέρας: " + systemDate);
                
                // Φόρτωση των πραγματικών αιτημάτων της ημέρας από τα CSV αρχεία του καθηγητή [cite: 56, 121]
                StorableList<Request> dailyRequests = storageManager.loadRequestsForDate(systemDate);
                
                // Δρομολόγηση επεξεργασίας [cite: 92, 121]
                requestProcessor.processDailyRequests(dailyRequests, systemDate);
                
                systemDate = systemDate.plusDays(1); // Πέρασμα στην επόμενη μέρα
            }
            System.out.println("Η προσομοίωση ολοκληρώθηκε.");
            
        } catch (DateTimeParseException e) {
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