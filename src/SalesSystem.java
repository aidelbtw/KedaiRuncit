import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SalesSystem {
    
    public static void sell(DataManager dm, Employee employee) {
        Scanner input = new Scanner(System.in);
        
        // Which Store prompt
        String outletCode = selectOutletForSale(dm, input, employee);
        if (outletCode == null) return;
        
        System.out.println("\n=== Record New Sale ===");
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Date: " + today.format(dateFormatter));
        System.out.println("Time: " + time.format(timeFormatter));
        System.out.println("Outlet: " + outletCode + " (" + dm.getOutletName(outletCode) + ")");
        
        System.out.print("Customer Name: ");
        String customerName = input.nextLine();
        
        List<String> itemsPurchased = new ArrayList<>();
        double totalPrice = 0.0;
        
        boolean hasMoreItems = true;
        while (hasMoreItems) {
            System.out.print("Enter Model: ");
            String model = input.nextLine();
            
            Product product = dm.getProductByModel(model);
            if (product == null) {
                System.out.println("Model not found!");
                continue;
            }
            
            // Check stock at the selected outlet
            int currentStock = product.getStockByOutletCode(outletCode, dm);
            if (currentStock <= 0) {
                System.out.println("No stock available for this model at " + outletCode + " (" + dm.getOutletName(outletCode) + ")");
                continue;
            }
            
            System.out.println("Available stock: " + currentStock);
            System.out.print("Enter Quantity: ");
            int quantity = input.nextInt();
            input.nextLine(); // consume newline
            
            if (quantity > currentStock) {
                System.out.println("Insufficient stock! Available: " + currentStock);
                continue;
            }
            
            double unitPrice = product.getPrice();
            double itemTotal = unitPrice * quantity;
            totalPrice += itemTotal;
            
            itemsPurchased.add(model + " (Quantity: " + quantity + ", Unit Price: RM" + String.format("%.2f", unitPrice) + ")");
            
            // Update stock at the selected outlet
            product.setStockByOutletCode(outletCode, dm, currentStock - quantity);
            
            System.out.println("Unit Price: RM" + String.format("%.2f", unitPrice));
            System.out.println("Total: RM " + String.format("%.2f", unitPrice) + " x" + quantity + " = RM" + String.format("%.2f", unitPrice*quantity));
            System.out.print("\nAre there more items purchased? (Y/N): ");
            String more = input.nextLine();
            if (!more.toUpperCase().equals("Y")) {
                hasMoreItems = false;
            }
        }
        
        if (itemsPurchased.isEmpty()) {
            System.out.println("No items purchased. Sale cancelled.");
            return;
        }
        
        System.out.print("\nEnter transaction method: ");
        String transactionMethod = input.nextLine();
        
        System.out.println("Subtotal: RM" + String.format("%.2f", totalPrice));
        
        // Save updated stock
        dm.saveProducts();
        System.out.println("Transaction successful.");
        System.out.println("Sale recorded successfully.");
        System.out.println("Model quantities updated successfully.");
        
        // Generate sales receipt
        generateSalesReceipt(today, time, customerName, itemsPurchased, 
                           transactionMethod, totalPrice, employee.getEmployeeName(), outletCode, dm.getOutletName(outletCode));
    }
    
    private static String selectOutletForSale(DataManager dm, Scanner input, Employee employee) {
        System.out.println("\n=== Select Sale Outlet ===");
        System.out.println("Available outlets:");
        
        // Show only outlets that are not HQ
        List<String> nonHQOutlets = new ArrayList<>();
        for (int i = 0; i < dm.getOutletCodes().size(); i++) {
            String code = dm.getOutletCodes().get(i);
            if (!code.equals("HQ")) {
                System.out.println(code + " - " + dm.getOutletName(code));
                nonHQOutlets.add(code);
            }
        }
        
        System.out.print("Enter outlet code for this sale: ");
        String outletCode = input.nextLine().trim().toUpperCase();
        
        if (nonHQOutlets.contains(outletCode)) {
            return outletCode;
        } else {
            System.out.println("Invalid outlet code!");
            return null;
        }
    }
    
    private static void generateSalesReceipt(LocalDate date, LocalTime time, String customerName,
                                          List<String> items, String transactionMethod, 
                                          double totalPrice, String employeeName, String outletCode, String outletName) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        String filename = "sales/sales_" + date.format(dateFormatter) + ".txt";
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) { // Append mode
            pw.println("=== SALES RECEIPT ===");
            pw.println("Date: " + date.format(dateFormatter));
            pw.println("Time: " + time.format(timeFormatter));
            pw.println("Outlet: " + outletCode + " (" + outletName + ")");
            pw.println("Customer Name: " + customerName);
            pw.println("Items Purchased: ");
            for (String item : items) {
                pw.println("- " + item);
            }
            pw.println("Transaction Method: " + transactionMethod);
            pw.println("Total Price: RM" + String.format("%.2f", totalPrice));
            pw.println("Employee: " + employeeName);
            pw.println("=====================================");
            pw.println();
        } catch (IOException e) {
            System.out.println("Error generating receipt: " + e.getMessage());
        }
        
        System.out.println("Receipt generated: " + filename);
    }
}