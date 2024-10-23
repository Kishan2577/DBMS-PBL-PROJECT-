package StorageManager;
import java.io.*;
import java.util.*;

import Table.Table;

public class StorageManager {
    private Map<String, Table> tables;
    private static final String STORAGE_FILE = "database_storage.txt";  // File to store tables and records

    public StorageManager() {
        this.tables = new HashMap<>();
        loadFromFile();  // Load existing tables and data from file on initialization
    }

    // Create a new table with schema
    public void createTable(String query) {
        // Example query: CREATE TABLE Employee (ID INT, Name VARCHAR, Salary FLOAT)
        String[] parts = query.split("\\(", 2);
        String tableName = parts[0].split(" ")[2];  // Get table name
        String schemaString = parts[1].replace(")", "").trim();  // Get schema part

        Map<String, String> schema = new LinkedHashMap<>();  // Maintain insertion order
        String[] columns = schemaString.split(",");
        for (String column : columns) {
            String[] columnParts = column.trim().split(" ");
            String columnName = columnParts[0];
            String columnType = columnParts[1];
            schema.put(columnName, columnType);
        }

        if (tables.containsKey(tableName)) {
            System.out.println("Table already exists.");
        } else {
            Table table = new Table(tableName, schema);
            tables.put(tableName, table);
            System.out.println("Table " + tableName + " created with schema: " + schema);
            saveToFile();  // Save updated tables to file
        }
    }

    // Insert a new record into a table with validation against schema
    public void insertRecord(String query) {
        // Example query: INSERT INTO Employee VALUES (1, 'John', 5000.00)
        String[] parts = query.split(" ");
        String tableName = parts[2];
        String values = query.substring(query.indexOf("VALUES") + 7);  // Extract the values part
        Table table = tables.get(tableName);

        if (table != null) {
            boolean success = table.insert(values);
            if (success) {
                System.out.println("Record inserted into " + tableName);
                saveToFile();  // Save updated records to file
            } else {
                System.out.println("Failed to insert record due to schema validation.");
            }
        } else {
            System.out.println("Table not found.");
        }
    }

    // Select all records from a table
    public void selectRecords(String query) {
        String[] parts = query.split(" ");
        String tableName = parts[3];
        Table table = tables.get(tableName);

        if (table != null) {
            table.selectAll();
        } else {
            System.out.println("Table not found.");
        }
    }

    // Save tables and records to a file in text format
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORAGE_FILE))) {
            for (Table table : tables.values()) {
                writer.write("TABLE: " + table.getName() + "\n");
                writer.write("SCHEMA: " + table.getSchema() + "\n");
                for (String record : table.getRecords()) {
                    writer.write("RECORD: " + record + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load tables and records from a file
    private void loadFromFile() {
        File file = new File(STORAGE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Table currentTable = null;
                Map<String, String> schema = new LinkedHashMap<>();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("TABLE: ")) {
                        String tableName = line.substring(7);
                        schema = new LinkedHashMap<>();
                        currentTable = new Table(tableName, schema);
                        tables.put(tableName, currentTable);
                    } else if (line.startsWith("SCHEMA: ") && currentTable != null) {
                        String schemaStr = line.substring(8);
                        String[] columns = schemaStr.replace("{", "").replace("}", "").split(",");
                        for (String column : columns) {
                            String[] columnParts = column.split("=");
                            schema.put(columnParts[0].trim(), columnParts[1].trim());
                        }
                    } else if (line.startsWith("RECORD: ") && currentTable != null) {
                        String record = line.substring(8);
                        currentTable.insert(record);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
    }
}
