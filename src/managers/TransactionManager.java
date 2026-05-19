package managers;

import entities.user.Customer;
import finance.Transaction;
import finance.Statement;

public class TransactionManager {
    private StatementManager statementManager;

    public TransactionManager(StatementManager statementManager) {
        this.statementManager = statementManager;
    }

    public void executeTransaction(Customer customer, Transaction transaction) {
        // 1. Let Wallet natively calculate balance shift & append the Statement [cite: 40]
        customer.getWallet().addTransaction(transaction, "System");

        // 2. Safely relay the newly recorded state to the persistence manager
        if (!customer.getWallet().getHistory().isEmpty()) {
            Statement latestStatement = customer.getWallet().getHistory().get(customer.getWallet().getHistory().size() - 1);
            statementManager.recordStatement(customer.getVat(), latestStatement);
        }
    }
}
