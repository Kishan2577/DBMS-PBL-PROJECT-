public class QueryProcessor {
    private StorageManager storageManager;
    private TransactionManager transactionManager;

    public QueryProcessor() {
        this.storageManager = new StorageManager();
        this.transactionManager = new TransactionManager();
    }

    public void processQuery(String query) {
        if (query.startsWith("CREATE TABLE")) {
            // Handle table creation
            storageManager.createTable(query);
        } else if (query.startsWith("INSERT INTO")) {
            // Handle data insertion
            storageManager.insertRecord(query);
        } else if (query.startsWith("SELECT")) {
            // Handle data retrieval
            storageManager.selectRecords(query);
        }/* else if (query.startsWith("UPDATE")) {
            // Handle data update
            storageManager.updateRecord(query);
        } else if (query.startsWith("DELETE")) {
            // Handle data deletion
            storageManager.deleteRecord(query);
        } */else if (query.startsWith("BEGIN TRANSACTION")) {
            // Handle transaction start
            transactionManager.beginTransaction();
        } else if (query.startsWith("COMMIT")) {
            // Handle transaction commit
            transactionManager.commit();
        } else if (query.startsWith("ROLLBACK")) {
            // Handle transaction rollback
            transactionManager.rollback();
        } else {
            System.out.println("Invalid query.");
        }
    }
}
