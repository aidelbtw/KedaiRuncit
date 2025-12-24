import java.io.*;
import java.util.*;

public class DataManager {

    private List<Employee> employees;
    private List<String> outletCodes;
    private List<String> outletNames;
    private List<Product> products;

    public DataManager() {
        employees = new ArrayList<>();
        outletCodes = new ArrayList<>();
        outletNames = new ArrayList<>();
        products = new ArrayList<>();
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
            System.out.println("Error reading employee file: " + filename + " - " + e.getMessage());
        }
    }

    public void loadOutlets(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header

            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    outletCodes.add(parts[0]);
                    outletNames.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading outlet file: " + filename + " - " + e.getMessage());
        }
    }

    public void loadProducts(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header

            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 12) { // At least model, price, and 10 outlet stocks
                    String model = parts[0];
                    double price;
                    try {
                        price = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing price for model " + model + ": " + parts[1]);
                        continue;
                    }
                    
                    int[] stockLevels = new int[10]; // C60 to C69
                    for (int i = 0; i < 10 && i + 2 < parts.length; i++) {
                        try {
                            stockLevels[i] = Integer.parseInt(parts[i + 2]);
                        } catch (NumberFormatException e) {
                            stockLevels[i] = 0; // Default to 0 if parsing fails
                        }
                    }
                    products.add(new Product(model, price, stockLevels));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading product file: " + filename + " - " + e.getMessage());
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

    public List<Product> getProducts() {
        return products;
    }

    public Product getProductByModel(String model) {
        for (Product product : products) {
            if (product.getModel().equals(model)) {
                return product;
            }
        }
        return null;
    }

    public List<String> getOutletCodes() {
        return outletCodes;
    }

    public void saveProducts() {
        // Use the same path structure as the load methods
        String savePath = "data/model.csv";
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(savePath))) {
            pw.println("\uFEFFModel,Price,C60,C61,C62,C63,C64,C65,C66,C67,C68,C69");
            for (Product product : products) {
                StringBuilder line = new StringBuilder();
                line.append(product.getModel()).append(",");
                line.append(product.getPrice()).append(",");
                for (int i = 0; i < 10; i++) {
                    line.append(product.getStockByOutletIndex(i));
                    if (i < 9) line.append(",");
                }
                pw.println(line.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + savePath + " - " + e.getMessage());
        }
    }
    
    // Keep the old method for compatibility but use the new one
    public void saveProducts(String filename) {
        saveProducts(); // Use the new method that finds the correct path
    }
}