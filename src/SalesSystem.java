import java.io.*;
import java.time.*;
import java.util.*;

public class SalesSystem {
    
    public static void sell(DataManager dm, Employee employee) {
        Scanner input = new Scanner(System.in);
        String outletCode = employee.getOutlet();
        
        if(outletCode.equals("HQ") || outletCode.length() < 3) {
             System.out.println("Available Outlets: " + dm.getOutletCodes());
             System.out.print("Enter Outlet Code: ");
             outletCode = input.nextLine().toUpperCase();
        }
        
        if (dm.getOutletIndex(outletCode) == -1) {
            System.out.println("Invalid Outlet.");
            return;
        }

        System.out.println("\n=== New Sale at " + outletCode + " ===");
        System.out.print("Customer Name: ");
        String customer = input.nextLine();
        
        List<String> cartItems = new ArrayList<>();
        double total = 0.0;
        
        while(true) {
            System.out.print("Enter Model (or 'done'): ");
            String model = input.nextLine();
            if(model.equalsIgnoreCase("done")) break;

            Product p = dm.getProductByModel(model);
            if(p == null) {
                System.out.println("Model not found.");
                continue;
            }

            int stock = p.getStockByOutletCode(outletCode, dm);
            System.out.println("Stock: " + stock + " | Price: RM" + p.getPrice());
            System.out.print("Qty: ");
            int qty = input.nextInt(); input.nextLine();

            if(qty > stock) {
                System.out.println("Not enough stock.");
            } else {
                p.setStockByOutletCode(outletCode, dm, stock - qty);
                double sub = p.getPrice() * qty;
                total += sub;
                cartItems.add(model + ":" + qty + ":" + sub);
                System.out.println("Added.");
            }
        }

        if(total == 0) return;

        System.out.println("Total: RM" + String.format("%.2f", total));
        System.out.print("Payment Method (Cash/Card/QR): ");
        String method = input.nextLine();

        dm.saveProducts();
        saveReceipt(outletCode, customer, cartItems, total, method, employee);
        logSaleForAnalytics(outletCode, customer, total, employee.getEmployeeID());
        System.out.println("Sale Complete.");
    }

    private static void saveReceipt(String outlet, String cust, List<String> items, double total, String method, Employee emp) {
        // PATH: ../sales/Receipt...
        String filename = "../sales/Receipt_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("=== RECEIPT ===");
            pw.println("Date: " + LocalDate.now());
            pw.println("Outlet: " + outlet);
            pw.println("Customer: " + cust);
            for(String i : items) pw.println("- " + i);
            pw.println("Total: RM" + total);
            pw.println("Staff: " + emp.getEmployeeName());
            pw.println("Method: " + method);
        } catch (IOException e) { System.out.println("Receipt saved to " + filename); }
    }

    private static void logSaleForAnalytics(String outlet, String cust, double total, String empID) {
        // PATH: ../data/sales_history.csv
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/sales_history.csv", true))) {
            pw.println(LocalDate.now() + "," + outlet + "," + total + "," + empID);
        } catch (IOException e) { System.out.println("Log Error: " + e.getMessage()); }
    }
}
