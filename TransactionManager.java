public class TransactionManager {
    public void beginTransaction() {
        System.out.println("Transaction started.");
    }

    public void commit() {
        System.out.println("Transaction committed.");
    }

    public void rollback() {
        System.out.println("Transaction rolled back.");
    }
}
