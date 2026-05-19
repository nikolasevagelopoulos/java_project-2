package managers;

import finance.Statement;
import storage.StorableList;

public class StatementManager {
    private StorableList<Statement> allStatements;

    public StatementManager() {
        this.allStatements = new StorableList<>();
    }

    // Δημιουργία και αποθήκευση κίνησης 
    public void recordStatement(String vat, Statement statement) {
        // Στην πραγματικότητα, ίσως θες να τα σώσεις στο αρχείο: 
        // data/statements/<VAT>.csv [cite: 105]
        allStatements.add(statement);
    }
}