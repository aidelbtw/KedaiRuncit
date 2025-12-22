import java.io.*;
import java.util.*;

public class DataManager {

    private List<Employee> employees;
    private List<String> outletCodes;
    private List<String> outletNames;

    public DataManager() {
        employees = new ArrayList<>();
        outletCodes = new ArrayList<>();
        outletNames = new ArrayList<>();
    }

    public void loadEmployees(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();

            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    employees.add(new Employee(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file: " + filename);
        }
    }

    public void loadOutlets(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    outletCodes.add(parts[0]);
                    outletNames.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading outlet file: " + filename);
        }
    }

    public String getOutletName(String outletCode) {
        for (int i = 0; i < outletCodes.size(); i++) {
            if (outletCodes.get(i).equals(outletCode)) {
                return outletNames.get(i);
            }
        }
        return "Unknown Outlet";
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
