import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockManagement {
    
    public static void manage(DataManager dm, Employee employee) {
        Scanner input = new Scanner(System.in);
        boolean goBack = false;
        
        while (!goBack) {
            System.out.println("\n===== Stock Management Menu =====");
            System.out.println("1. Morning Stock Count");
            System.out.println("2. Night Stock Count");
            System.out.println("3. Stock In");
            System.out.println("4. Stock Out");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            int choice = input.nextInt();
            input.nextLine();
            
            switch (choice) {
                case 1:
                    morningStockCount(dm, employee, input);
                    break;
                case 2:
                    nightStockCount(dm, employee, input);
                    break;
                case 3:
                    stockIn(dm, employee, input);
                    break;
                case 4:
                    stockOut(dm, employee, input);
                    break;
                case 5:
                    goBack = true;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    private static void morningStockCount(DataManager dm, Employee employee, Scanner input) {
        String outletCode = selectOutletForCounting(dm, input, "Morning Stock Count");
        if (outletCode == null) return;
        
        System.out.println("\n=== Morning Stock Count ===");
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Date: " + today.format(dateFormatter));
        System.out.println("Time: " + time.format(timeFormatter));
        System.out.println("Outlet: " + outletCode + " (" + dm.getOutletName(outletCode) + ")");
        System.out.println();
        
        int tallyCorrect = 0;
        int mismatches = 0;
        
        for (Product product : dm.getProducts()) {
            int storeRecord = product.getStockByOutletCode(outletCode, dm);
            System.out.print("Model: " + product.getModel() + " – Counted: ");
            int counted = input.nextInt();
            
            System.out.println("Store Record: " + storeRecord);
            
            if (counted == storeRecord) {
                System.out.println("Stock tally correct.\n");
                tallyCorrect++;
            } else {
                int difference = Math.abs(counted - storeRecord);
                System.out.println("! Mismatch detected (" + difference + " unit difference)\n");
                mismatches++;
            }
        }
        
        System.out.println("Total Models Checked: " + (tallyCorrect + mismatches));
        System.out.println("Tally Correct: " + tallyCorrect);
        System.out.println("Mismatches: " + mismatches);
        System.out.println("Morning stock count completed.");
        if (mismatches > 0) {
            System.out.println("Warning: Please verify stock.");
        }
    }
    
    private static void nightStockCount(DataManager dm, Employee employee, Scanner input) {
        String outletCode = selectOutletForCounting(dm, input, "Night Stock Count");
        if (outletCode == null) return;
        
        System.out.println("\n=== Night Stock Count ===");
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Date: " + today.format(dateFormatter));
        System.out.println("Time: " + time.format(timeFormatter));
        System.out.println("Outlet: " + outletCode + " (" + dm.getOutletName(outletCode) + ")");
        System.out.println();
        
        int tallyCorrect = 0;
        int mismatches = 0;
        
        for (Product product : dm.getProducts()) {
            int storeRecord = product.getStockByOutletCode(outletCode, dm);
            System.out.print("Model: " + product.getModel() + " – Counted: ");
            int counted = input.nextInt();
            
            System.out.println("Store Record: " + storeRecord);
            
            if (counted == storeRecord) {
                System.out.println("Stock tally correct.\n");
                tallyCorrect++;
            } else {
                int difference = Math.abs(counted - storeRecord);
                System.out.println("! Mismatch detected (" + difference + " unit difference)\n");
                mismatches++;
            }
        }
        
        System.out.println("Total Models Checked: " + (tallyCorrect + mismatches));
        System.out.println("Tally Correct: " + tallyCorrect);
        System.out.println("Mismatches: " + mismatches);
        System.out.println("Night stock count completed.");
        if (mismatches > 0) {
            System.out.println("Warning: Please verify stock.");
        }
    }
    
    private static String selectOutletForCounting(DataManager dm, Scanner input, String operationType) {
        System.out.println("\n=== " + operationType + " ===");
        System.out.println("Available outlets:");
        
        
        List<String> nonHQOutlets = new ArrayList<>();
        for (int i = 0; i < dm.getOutletCodes().size(); i++) {
            String code = dm.getOutletCodes().get(i);
            if (!code.equals("HQ")) {
                System.out.println(code + " - " + dm.getOutletName(code));
                nonHQOutlets.add(code);
            }
        }
        
        System.out.print("Enter outlet code: ");
        String outletCode = input.nextLine().trim().toUpperCase();
        
        if (nonHQOutlets.contains(outletCode)) {
            return outletCode;
        } else {
            System.out.println("Invalid outlet code!");
            return null;
        }
    }
    
    private static void stockIn(DataManager dm, Employee employee, Scanner input) {
        System.out.println("\n=== Stock In ===");
        
        // Select destination outlet
        String destinationOutlet = selectOutletForStockMovement(dm, input, "Stock In", "destination");
        if (destinationOutlet == null) return;
        
        // Select source outlet (including HQ)
        String sourceOutlet = selectOutletForStockMovementWithHQ(dm, input, "Stock In", "source");
        if (sourceOutlet == null) return;
        
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Date: " + today.format(dateFormatter));
        System.out.println("Time: " + time.format(timeFormatter));
        System.out.println("From: " + sourceOutlet + " (" + dm.getOutletName(sourceOutlet) + ")");
        System.out.println("To: " + destinationOutlet + " (" + dm.getOutletName(destinationOutlet) + ")");
        
        List<String> modelsReceived = new ArrayList<>();
        int totalQuantity = 0;
        
        boolean hasMore = true;
        while (hasMore) {
            System.out.print("Enter Model: ");
            String model = input.nextLine();
            
            Product product = dm.getProductByModel(model);
            if (product == null) {
                System.out.println("Model not found!");
                continue;
            }
            
            System.out.print("Enter Quantity: ");
            int quantity = input.nextInt();
            input.nextLine(); // consume newline
            
            // Update stock at destination outlet
            int destStock = product.getStockByOutletCode(destinationOutlet, dm);
            product.setStockByOutletCode(destinationOutlet, dm, destStock + quantity);
            
            // If source is not HQ, also reduce from source
            if (!sourceOutlet.equals("HQ")) {
                int sourceStock = product.getStockByOutletCode(sourceOutlet, dm);
                if (quantity > sourceStock) {
                    System.out.println("Insufficient stock in source outlet! Available: " + sourceStock);
                    continue;
                }
                product.setStockByOutletCode(sourceOutlet, dm, Math.max(0, sourceStock - quantity));
            }
            
            modelsReceived.add(model + " (Quantity: " + quantity + ")");
            totalQuantity += quantity;
            
            System.out.print("Are there more models? (Y/N): ");
            String more = input.nextLine();
            if (!more.toUpperCase().equals("Y")) {
                hasMore = false;
            }
        }
        
        if (modelsReceived.isEmpty()) {
            System.out.println("No items processed.");
            return;
        }
        
        System.out.println("Models Received: ");
        for (String model : modelsReceived) {
            System.out.println("- " + model);
        }
        System.out.println("Total Quantity: " + totalQuantity);
        
        dm.saveProducts(); // Save updated stock - using the new method that finds the correct path
        System.out.println("Model quantities updated successfully.");
        System.out.println("Stock In recorded.");
        
        // Generate receipt
        generateStockReceipt("Stock In", today, time, 
                           sourceOutlet + " (" + dm.getOutletName(sourceOutlet) + ")", 
                           destinationOutlet + " (" + dm.getOutletName(destinationOutlet) + ")", 
                           modelsReceived, totalQuantity, employee.getEmployeeName());
    }
    
    private static void stockOut(DataManager dm, Employee employee, Scanner input) {
        System.out.println("\n=== Stock Out ===");
        
        // Select source outlet (including HQ)
        String sourceOutlet = selectOutletForStockMovementWithHQ(dm, input, "Stock Out", "source");
        if (sourceOutlet == null) return;
        
        // Select destination outlet (excluding HQ for stock out)
        String destinationOutlet = selectOutletForStockMovement(dm, input, "Stock Out", "destination");
        if (destinationOutlet == null) return;
        
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.println("Date: " + today.format(dateFormatter));
        System.out.println("Time: " + time.format(timeFormatter));
        System.out.println("From: " + sourceOutlet + " (" + dm.getOutletName(sourceOutlet) + ")");
        System.out.println("To: " + destinationOutlet + " (" + dm.getOutletName(destinationOutlet) + ")");
        
        List<String> modelsTransferred = new ArrayList<>();
        int totalQuantity = 0;
        
        boolean hasMore = true;
        while (hasMore) {
            System.out.print("Enter Model: ");
            String model = input.nextLine();
            
            Product product = dm.getProductByModel(model);
            if (product == null) {
                System.out.println("Model not found!");
                continue;
            }
            
            int currentStock = product.getStockByOutletCode(sourceOutlet, dm);
            if (currentStock <= 0) {
                System.out.println("Insufficient stock!");
                continue;
            }
            
            System.out.print("Enter Quantity: ");
            int quantity = input.nextInt();
            input.nextLine(); // consume newline
            
            if (quantity > currentStock) {
                System.out.println("Quantity exceeds available stock! Available: " + currentStock);
                continue;
            }
            
            // Update stock at source outlet
            product.setStockByOutletCode(sourceOutlet, dm, currentStock - quantity);
            
            // Update stock at destination outlet
            int destStock = product.getStockByOutletCode(destinationOutlet, dm);
            product.setStockByOutletCode(destinationOutlet, dm, destStock + quantity);
            
            modelsTransferred.add(model + " (Quantity: " + quantity + ")");
            totalQuantity += quantity;
            
            System.out.print("Are there more models? (Y/N): ");
            String more = input.nextLine();
            if (!more.toUpperCase().equals("Y")) {
                hasMore = false;
            }
        }
        
        if (modelsTransferred.isEmpty()) {
            System.out.println("No items processed.");
            return;
        }
        
        System.out.println("Models Transferred: ");
        for (String model : modelsTransferred) {
            System.out.println("- " + model);
        }
        System.out.println("Total Quantity: " + totalQuantity);
        
        dm.saveProducts(); // Save updated stock 
        System.out.println("Model quantities updated successfully.");
        System.out.println("Stock Out recorded.");
        
        // Generate receipt
        generateStockReceipt("Stock Out", today, time, 
                           sourceOutlet + " (" + dm.getOutletName(sourceOutlet) + ")", 
                           destinationOutlet + " (" + dm.getOutletName(destinationOutlet) + ")", 
                           modelsTransferred, totalQuantity, employee.getEmployeeName());
    }
    
    private static String selectOutletForStockMovement(DataManager dm, Scanner input, String operationType, String type) {
        System.out.println("\nSelect " + type + " outlet for " + operationType );
        
      
        List<String> nonHQOutlets = new ArrayList<>();
        for (int i = 0; i < dm.getOutletCodes().size(); i++) {
            String code = dm.getOutletCodes().get(i);
            if (!code.equals("HQ")) {
                System.out.println(code + " - " + dm.getOutletName(code));
                nonHQOutlets.add(code);
            }
        }
        
        System.out.print("Enter outlet code: ");
        String outletCode = input.nextLine().trim().toUpperCase();
        
        if (nonHQOutlets.contains(outletCode)) {
            return outletCode;
        } else {
            System.out.println("Invalid outlet code!");
            return null;
        }
    }
    
    private static String selectOutletForStockMovementWithHQ(DataManager dm, Scanner input, String operationType, String type) {
        System.out.println("\nSelect " + type + " outlet for " + operationType + " (including HQ):");
        
        // Show all outlets including HQ
        for (int i = 0; i < dm.getOutletCodes().size(); i++) {
            String code = dm.getOutletCodes().get(i);
            System.out.println(code + " - " + dm.getOutletName(code));
        }
        
        System.out.print("Enter outlet code: ");
        String outletCode = input.nextLine().trim().toUpperCase();
        
        if (dm.getOutletCodes().contains(outletCode)) {
            return outletCode;
        } else {
            System.out.println("Invalid outlet code!");
            return null;
        }
    }
    
    private static void generateStockReceipt(String transactionType, LocalDate date, LocalTime time,
                                           String from, String to, List<String> models, 
                                           int totalQuantity, String employeeName) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        String filename = "receipts/receipts_" + date.format(dateFormatter) + ".txt";
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) { // Append mode
            pw.println("=== " + transactionType + " RECEIPT ===");
            pw.println("Date: " + date.format(dateFormatter));
            pw.println("Time: " + time.format(timeFormatter));
            pw.println("From: " + from);
            pw.println("To: " + to);
            pw.println("Models: ");
            for (String model : models) {
                pw.println("- " + model);
            }
            pw.println("Total Quantity: " + totalQuantity);
            pw.println("Employee: " + employeeName);
            pw.println("=====================================");
            pw.println();
        } catch (IOException e) {
            System.out.println("Error generating receipt: " + e.getMessage());
        }
        
        System.out.println("Receipt generated: " + filename);
    }
}