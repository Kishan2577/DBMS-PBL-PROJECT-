package Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table implements Serializable {
    private String name;
    private List<String> records;
    private Map<String, String> schema;  // Column name and type

    public Table(String name, Map<String, String> schema) {
        this.name = name;
        this.records = new ArrayList<>();
        this.schema = schema;
    }

    // Insert data with validation
    public boolean insert(String record) {
        String[] values = record.split(",");
        if (values.length != schema.size()) {
            System.out.println("Error: Number of values does not match schema.");
            return false;
        }

        // Validate each value according to the schema
        int index = 0;
        for (Map.Entry<String, String> entry : schema.entrySet()) {
            String columnName = entry.getKey();
            String columnType = entry.getValue();
            String value = values[index].trim();

            if (!validateValue(value, columnType)) {
                System.out.println("Error: Value " + value + " does not match type " + columnType + " for column " + columnName);
                return false;
            }
            index++;
        }

        // If validation passes, insert the record
        records.add(record);
        return true;
    }

    public void selectAll() {
        System.out.println("Records in " + name + ":");
        System.out.println("Schema: " + schema);
        for (String record : records) {
            System.out.println(record);
        }
    }

    public void update(String newValue) {
        if (!records.isEmpty()) {
            records.set(0, newValue);  // Update the first record (as an example)
        }
    }

    public void delete() {
        if (!records.isEmpty()) {
            records.remove(0);  // Delete the first record (as an example)
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getRecords() {
        return records;
    }

    public Map<String, String> getSchema() {
        return schema;
    }

    // Helper method to validate value according to the column type
    // Helper method to validate value according to the column type
private boolean validateValue(String value, String columnType) {
    switch (columnType.toUpperCase()) {
        case "INT":
            return value.matches("-?\\d+");  // Check if the value is an integer (allows negative values too)
        case "VARCHAR":
            return value.startsWith("\"") && value.endsWith("\"");  // Check if the value is a string (e.g., "KISHAN")
        case "FLOAT":
            try {
                Float.parseFloat(value);  // Attempt to parse the value as a float
                return true;
            } catch (NumberFormatException e) {
                return false;  // Value is not a valid float
            }
        default:
            return false;
    }
}

}
