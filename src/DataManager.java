import java.io.*;
import java.util.*;

public class DataManager {
    private List<Employee> employees;

    public DataManager() {
        employees = new ArrayList<>();
    }

    public void loadEmployees(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header

            if (line != null && line.startsWith("\uFEFF")) { // remove BOM
                line = line.substring(1);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Employee emp = new Employee(parts[0], parts[1], parts[2], parts[3]);
                    employees.add(emp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
