import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockManagement {

    private static Scanner input = new Scanner(System.in);

    // WHITELIST: The exact names of your outlets/columns in CSV
    private static final List<String> VALID_OUTLETS = Arrays.asList(
        "C60", "C61", "C62", "C63", "C64", "C65", "C66", "C67", "C68", "C69", "HQ"
    );

    public static void manage(DataManager dm, Employee emp) {
        while (true) {
            System.out.println("\n=== STOCK MANAGEMENT (" + emp.getOutlet() + ") ===");
            System.out.println("1. Perform Stock Count");
            System.out.println("2. Stock In");
            System.out.println("3. Stock Out");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) { continue; }

            switch (choice) {
                case 1: performStockCount(dm, emp); break; // Pass emp
                case 2: handleStockMovement(dm, emp, true); break;  // Stock In
                case 3: handleStockMovement(dm, emp, false); break; // Stock Out
                case 4: return;
            }
        }
    }

    private static void performStockCount(DataManager dm, Employee emp) {
        // === FIX: Auto-detect outlet ===
        String outletCode = emp.getOutlet();
        System.out.println("\n=== Stock Count for " + outletCode + " ===");

        System.out.println("1. Morning | 2. Night");
        String session = input.nextLine().equals("1") ? "Morning" : "Night";
        
        int mismatches = 0;
        
        for (Product p : dm.getProducts()) {
            int systemStock = p.getStockByOutletCode(outletCode, dm);

            System.out.print(p.getModel() + " (System: " + systemStock + ") - Counted: ");
            try {
                String val = input.nextLine();
                if(val.isEmpty()) continue;
                int counted = Integer.parseInt(val);
                
                if (counted != systemStock) {
                    System.out.println(">> ! MISMATCH DETECTED !");
                    mismatches++;
                }
            } catch (Exception e) {}
        }
        System.out.println("Done. Mismatches: " + mismatches);
    }

    private static void handleStockMovement(DataManager dm, Employee emp, boolean isStockIn) {
        String typeHeader = isStockIn ? "=== Stock In ===" : "=== Stock Out ===";
        System.out.println("\n" + typeHeader);
        
        String fromLocation;
        String toLocation;
        String currentOutlet = emp.getOutlet(); // === FIX: Get Session Outlet ===

        // === FIX: Auto-fill logic ===
        if (isStockIn) {
            // WE are receiving stock. Destination is US.
            toLocation = currentOutlet;
            System.out.println("Receiving Stock INTO: " + toLocation);
            
            // Only ask where it came from
            fromLocation = getValidOutletInput("Source (From Outlet/HQ): ");
            if (fromLocation == null) return;

        } else {
            // WE are sending stock. Source is US.
            fromLocation = currentOutlet;
            System.out.println("Sending Stock FROM: " + fromLocation);
            
            // Only ask where it is going
            toLocation = getValidOutletInput("Destination (To Outlet/HQ): ");
            if (toLocation == null) return;
            
            if (toLocation.equalsIgnoreCase("HQ")) {
                 System.out.println(">> Warning: Returning stock to HQ.");
            }
        }

        // 2. SELECT ITEMS & VALIDATE SOURCE STOCK
        List<Product> selectedProducts = new ArrayList<>();
        List<Integer> selectedQuantities = new ArrayList<>();
        int totalQty = 0;

        while(true) {
            System.out.print("Enter Model (or 'done'): ");
            String model = input.nextLine();
            if(model.equalsIgnoreCase("done")) break;

            Product p = dm.getProduct(model);
            if(p == null) { System.out.println("Error: Model not found."); continue; }

            System.out.print("Quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number."); continue;
            }

            // CHECK SOURCE STOCK
            if (!fromLocation.equalsIgnoreCase("HQ")) {
                int stockAtSource = p.getStockByOutletCode(fromLocation, dm);
                if (qty > stockAtSource) {
                    System.out.println(">> ERROR: " + fromLocation + " only has " + stockAtSource + " units.");
                    System.out.println(">> Cannot transfer " + qty + ".");
                    continue; 
                }
            }

            selectedProducts.add(p);
            selectedQuantities.add(qty);
            totalQty += qty;
        }

        if(totalQty == 0) return;

        // 3. UPDATE DATABASE
        for(int i = 0; i < selectedProducts.size(); i++) {
            Product p = selectedProducts.get(i);
            int qty = selectedQuantities.get(i);

            if (!fromLocation.equalsIgnoreCase("HQ")) {
                int currentSource = p.getStockByOutletCode(fromLocation, dm);
                p.setStockByOutletCode(fromLocation, dm, currentSource - qty);
            }

            if (!toLocation.equalsIgnoreCase("HQ")) {
                int currentDest = p.getStockByOutletCode(toLocation, dm);
                p.setStockByOutletCode(toLocation, dm, currentDest + qty);
            }
        }
        dm.saveProducts();

        // 4. GENERATE RECEIPT
        LocalDate date = LocalDate.now();
        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
                          .toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");

        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append(typeHeader).append("\n");
        receiptContent.append("Date: ").append(date).append("\n");
        receiptContent.append("Time: ").append(timeStr).append("\n");
        receiptContent.append("From: ").append(fromLocation).append("\n");
        receiptContent.append("To: ").append(toLocation).append("\n");
        receiptContent.append(isStockIn ? "Models Received:" : "Models Sent:").append("\n");
        
        for(int i = 0; i < selectedProducts.size(); i++) {
            receiptContent.append("- ").append(selectedProducts.get(i).getModel())
                          .append(" (Quantity: ").append(selectedQuantities.get(i)).append(")\n");
        }
        receiptContent.append("Total Quantity: ").append(totalQty).append("\n");
        receiptContent.append("Employee in Charge: ").append(emp.getName()).append("\n"); 
        receiptContent.append("\n"); 

        String filename = "../sales/receipts_" + date + ".txt";
        try (FileWriter fw = new FileWriter(filename, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(receiptContent.toString());
        } catch (IOException e) {
            System.out.println("Error saving receipt.");
        }

        System.out.println("Transfer recorded successfully.");
        System.out.println("Receipt generated: " + filename);
    }

    private static String getValidOutletInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String rawInput = input.nextLine().trim();

            if (rawInput.equalsIgnoreCase("cancel")) return null;
            if (rawInput.isEmpty()) continue;

            for (String valid : VALID_OUTLETS) {
                if (valid.equalsIgnoreCase(rawInput)) {
                    return valid; 
                }
            }
            System.out.println(">> Error: Outlet '" + rawInput + "' does not exist.");
            System.out.println(">> Valid Outlets: " + VALID_OUTLETS);
        }
    }
}