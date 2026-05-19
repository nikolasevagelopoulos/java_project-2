package managers;

import entities.user.Customer;
import finance.Transaction;
import finance.Statement;
import storage.StorableList;

public class TransactionManager {
    private StatementManager statementManager;

    public TransactionManager(StatementManager statementManager) {
        this.statementManager = statementManager;
    }

    // Εκτέλεση μιας συναλλαγής 
    public void executeTransaction(Customer customer, Transaction transaction) {
        // 1. Ενημέρωση του πορτοφολιού του πελάτη 
        if (transaction.getType().equals("CHARGE")) {
            customer.getWallet().addDebt(transaction.getAmount()); // Αυξάνει το χρέος [cite: 42]
        } else if (transaction.getType().equals("CREDIT")) {
            customer.getWallet().addPayment(transaction.getAmount()); // Μειώνει το χρέος [cite: 46]
        }

        // 2. Δημιουργία κίνησης (Statement) μέσω του StatementManager 
        Statement statement = new Statement(
            java.time.LocalDate.now().toString(), 
            transaction.getType(), 
            "System", 
            transaction.getAmount(), 
            transaction.getDescription()
        );
        statementManager.recordStatement(customer.getVat(), statement);
    }
}