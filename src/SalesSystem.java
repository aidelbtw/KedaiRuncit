import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SalesSystem {

    private static Scanner input = new Scanner(System.in);

    public static void sell(DataManager dm, Employee user) {
        System.out.println("\n=== Record New Sale ===");
        
        LocalDate date = LocalDate.now();
        // === FIX START: Create User-Friendly Date String ===
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter); 
        // === FIX END ===

        LocalTime time = LocalTime.now();
        String timeStr = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
        
        System.out.println("Date: " + formattedDate); // Display friendly date
        System.out.println("Time: " + timeStr);

        // Auto-detect Outlet
        String outletCode = user.getOutlet();
        System.out.println("Session Outlet: " + outletCode);

        // Security: HQ usually cannot sell retail items
        if (outletCode.equalsIgnoreCase("HQ")) {
            System.out.println(">> ERROR: HQ cannot perform retail sales transactions.");
            return;
        }

        System.out.print("Customer Name: ");
        String customerName = input.nextLine();

        List<Product> soldProducts = new ArrayList<>();
        List<Integer> soldQuantities = new ArrayList<>();
        double subtotal = 0.0;

        System.out.println("Item(s) Purchased:");
        boolean addingItems = true;

        while (addingItems) {
            System.out.print("Enter Model: ");
            String model = input.nextLine();
            
            Product p = dm.getProduct(model);
            if (p == null) {
                System.out.println("Error: Model not found.");
                continue;
            }

            System.out.print("Enter Quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
                continue;
            }

            int currentStock = p.getStockByOutletCode(outletCode, dm);
            
            if (qty > currentStock) {
                System.out.println("Error: Insufficient stock at " + outletCode + ". Available: " + currentStock);
                continue;
            }

            soldProducts.add(p);
            soldQuantities.add(qty);
            
            double lineTotal = p.getPrice() * qty;
            subtotal += lineTotal;
            System.out.println("Unit Price: RM" + p.getPrice());

            System.out.print("Are there more items purchased? (Y/N): ");
            String choice = input.nextLine();
            if (choice.equalsIgnoreCase("N")) {
                addingItems = false;
            }
        }

        if (soldProducts.isEmpty()) {
            System.out.println("Transaction cancelled.");
            return;
        }

        System.out.print("Enter transaction method: ");
        String method = input.nextLine();

        System.out.println("Subtotal: RM" + String.format("%.2f", subtotal));
        
        // Update Stock
        for (int i = 0; i < soldProducts.size(); i++) {
            Product p = soldProducts.get(i);
            int qty = soldQuantities.get(i);
            int currentStock = p.getStockByOutletCode(outletCode, dm);
            
            p.setStockByOutletCode(outletCode, dm, currentStock - qty);
        }
        
        dm.saveProducts(); 
        System.out.println("Transaction successful.");

        // Generate Text Receipt (Pass formattedDate for display)
        saveReceipt(date, formattedDate, timeStr, customerName, outletCode, user.getName(), method, subtotal, soldProducts, soldQuantities);

        // Save to History (Keep raw date object for sorting)
        saveToHistory(date, soldProducts, soldQuantities);
    }

    private static void saveReceipt(LocalDate rawDate, String displayDate, String time, String customer, String outlet, String empName, String method, double total, List<Product> items, List<Integer> qtys) {
        // Keep filename as yyyy-mm-dd (rawDate) so files sort correctly in folders
        String filename = "../sales/sales_" + rawDate + ".txt";
        
        try (FileWriter fw = new FileWriter(filename, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("=========================================");
            pw.println("            OFFICIAL RECEIPT             ");
            pw.println("=========================================");
            pw.println("Date: " + displayDate + "  Time: " + time); // Use friendly date here
            pw.println("Outlet: " + outlet);
            pw.println("Served By: " + empName);
            pw.println("Customer: " + customer);
            pw.println("-----------------------------------------");
            pw.println(String.format("%-15s %-5s %10s", "Model", "Qty", "Price(RM)"));
            
            for (int i = 0; i < items.size(); i++) {
                Product p = items.get(i);
                int q = qtys.get(i);
                pw.println(String.format("%-15s %-5d %10.2f", p.getModel(), q, (p.getPrice() * q)));
            }
            
            pw.println("-----------------------------------------");
            pw.println("Total Amount:       RM" + String.format("%.2f", total));
            pw.println("Payment Method:     " + method);
            pw.println("=========================================");
            pw.println(""); 
            
            System.out.println("Receipt generated: " + filename);

        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }

    private static void saveToHistory(LocalDate date, List<Product> items, List<Integer> qtys) {
        // Always use raw 'date' (yyyy-mm-dd) here for Excel sorting compatibility
        try (FileWriter fw = new FileWriter("../data/sales_history.csv", true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            for(int i = 0; i < items.size(); i++) {
                Product p = items.get(i);
                int qty = qtys.get(i);
                double total = p.getPrice() * qty;
                pw.println(date + "," + p.getModel() + "," + qty + "," + total);
            }
        } catch (IOException e) {
            System.out.println("Error updating sales history.");
        }
    }
}