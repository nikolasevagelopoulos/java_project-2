package ui;

import java.io.File;

import managers.*;
import storage.StorageManager;

public class Main {
    public static void main(String[] args) {
    	System.out.println("Η Java ψάχνει το αρχείο εδώ: " + new File("data/agents/agents.csv").getAbsolutePath());
    	// 1. Initialize Storage Persistence Layer
        StorageManager storageManager = new StorageManager();
        
        // 2. Initialize and populate Managers with actual data files 
        UserManager userManager = new UserManager();
        userManager.setAllUsers(storageManager.loadUsers()); 
        
        // If the professor's agents.csv is missing an Admin, add a default fallback:
        if (userManager.getUserByUsername("admin") == null) {
            userManager.addUser(new entities.user.Admin("admin", "adminpass"));
        }

        VehicleManager vehicleManager = new VehicleManager(storageManager.loadFleet());
        ContractManager contractManager = new ContractManager(storageManager.loadContracts());
        System.out.println("--- DEBUG INFO ---");
        System.out.println("Φορτώθηκαν " + userManager.getAllUsers().size() + " χρήστες.");
        System.out.println("Φορτώθηκαν " + vehicleManager.getFleet().size() + " οχήματα.");
        System.out.println("------------------");
        StatementManager statementManager = new StatementManager();
        TransactionManager transactionManager = new TransactionManager(statementManager);
        
        // 3. Link Request routing infrastructure
        RequestProcessor requestProcessor = new RequestProcessor(userManager, vehicleManager, contractManager, transactionManager);
        
        // 4. Launch User Console Interface
        CLI cli = new CLI(userManager, vehicleManager, contractManager, statementManager, transactionManager, requestProcessor, storageManager);
        cli.start();
    }
}