import java.io.*;
import java.util.*;

public class DataManager {
    private List<Employee> employees = new ArrayList<>();
    private List<String> outletCodes = new ArrayList<>();
    private List<String> outletNames = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    // --- LOADERS ---
    public void loadEmployees(String filename) {
        employees.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); 
            if (line != null && line.startsWith("\uFEFF")) line = line.substring(1);
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) employees.add(new Employee(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) { System.out.println("Error loading employees: " + e.getMessage()); }
    }

    public void loadOutlets(String filename) {
        outletCodes.clear(); outletNames.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    outletCodes.add(parts[0]);
                    outletNames.add(parts[1]);
                }
            }
        } catch (IOException e) { System.out.println("Error loading outlets: " + e.getMessage()); }
    }

    public void loadProducts(String filename) {
        products.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    try {
                        String model = parts[0];
                        double price = Double.parseDouble(parts[1]);
                        int[] stock = new int[10];
                        for (int i = 0; i < 10; i++) stock[i] = Integer.parseInt(parts[i + 2]);
                        products.add(new Product(model, price, stock));
                    } catch (Exception e) {}
                }
            }
        } catch (IOException e) { System.out.println("Error loading products: " + e.getMessage()); }
    }

    // --- SAVERS (Look UP one level with ../) ---
    public void saveProducts() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/model.csv"))) {
            pw.println("\uFEFFModel,Price,C60,C61,C62,C63,C64,C65,C66,C67,C68,C69");
            for (Product p : products) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getModel()).append(",").append(p.getPrice()).append(",");
                for (int i = 0; i < 10; i++) {
                    sb.append(p.getStockByOutletIndex(i));
                    if (i < 9) sb.append(",");
                }
                pw.println(sb.toString());
            }
        } catch (IOException e) { System.out.println("Error saving products."); }
    }

    public void saveEmployees() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/employee.csv"))) {
            pw.println("EmployeeID,EmployeeName,Role,Password");
            for (Employee e : employees) {
                pw.println(e.getEmployeeID() + "," + e.getEmployeeName() + "," + e.getRole() + "," + e.getPassword());
            }
        } catch (IOException e) { System.out.println("Error saving employees."); }
    }

    // --- GETTERS ---
    public List<Employee> getEmployees() { return employees; }
    public List<String> getOutletCodes() { return outletCodes; }
    
    public Product getProductByModel(String model) {
        for (Product p : products) if (p.getModel().equalsIgnoreCase(model)) return p;
        return null;
    }
    public String getOutletName(String code) {
        int idx = outletCodes.indexOf(code);
        return (idx != -1) ? outletNames.get(idx) : "Unknown";
    }
    public int getOutletIndex(String code) { return outletCodes.indexOf(code); }

    // FIX 1: The Getter must match the List type
    public java.util.List<Product> getProducts() {
        return products;
    }

    // FIX 2: The missing helper method for Stock Management
    public Product getProduct(String modelName) {
        for (Product p : products) {
            if (p.getModel().equalsIgnoreCase(modelName)) {
                return p;
            }
        }
        return null;
    }
}
