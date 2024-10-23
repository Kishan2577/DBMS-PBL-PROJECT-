import java.util.Scanner;

public class DBMSController {
    private QueryProcessor queryProcessor;

    public DBMSController() {
        this.queryProcessor = new QueryProcessor();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your SQL-like queries:");

        while (true) {
            String query = scanner.nextLine();
            if (query.equalsIgnoreCase("exit")) {
                break;
            }
            queryProcessor.processQuery(query);
        }

        scanner.close();
    }
}
