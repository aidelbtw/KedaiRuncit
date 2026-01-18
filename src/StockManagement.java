import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockManagement {

    private static Scanner input = new Scanner(System.in);

    public static void manage(DataManager dm, Employee emp) {
        while (true) {
            System.out.println("\n=== STOCK MANAGEMENT ===");
            System.out.println("1. Perform Stock Count");
            System.out.println("2. Stock In");
            System.out.println("3. Stock Out");
            // Removed View Stock option
            System.out.println("4. Back"); 
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) { continue; }

            switch (choice) {
                case 1: performStockCount(dm); break;
                case 2: handleStockMovement(dm, emp, true); break;
                case 3: handleStockMovement(dm, emp, false); break;
                case 4: return; // Changed from 5 to 4
            }
        }
    }

    private static void performStockCount(DataManager dm) {
        System.out.println("\n=== Stock Count ===");
        System.out.println("1. Morning | 2. Night");
        String session = input.nextLine().equals("1") ? "Morning" : "Night";
        
        int mismatches = 0;
        
        // FIX: Use getProducts()
        for (Product p : dm.getProducts()) {
            // FIX: Pass 'dm' to getStockByOutletCode
            int systemStock = p.getStockByOutletCode("C60", dm);

            System.out.print(p.getModel() + " (System: " + systemStock + ") - Counted: ");
            try {
                int counted = Integer.parseInt(input.nextLine());
                if (counted != systemStock) {
                    System.out.println(">> ! MISMATCH DETECTED !");
                    mismatches++;
                }
            } catch (Exception e) {}
        }
        System.out.println("Done. Mismatches: " + mismatches);
    }

    private static void handleStockMovement(DataManager dm, Employee emp, boolean isStockIn) {
        String type = isStockIn ? "Stock In" : "Stock Out";
        System.out.println("=== " + type + " ===");
        
        System.out.print(isStockIn ? "From: " : "To: ");
        String otherParty = input.nextLine();

        List<String> names = new ArrayList<>();
        List<Integer> qtys = new ArrayList<>();
        int totalQty = 0;

        while(true) {
            System.out.print("Model (or 'done'): ");
            String model = input.nextLine();
            if(model.equalsIgnoreCase("done")) break;

            // FIX: Uses the new getProduct method
            Product p = dm.getProduct(model);
            if(p == null) { System.out.println("Not found."); continue; }

            System.out.print("Qty: ");
            int qty = Integer.parseInt(input.nextLine());

            // FIX: Pass 'dm' here too
            int current = p.getStockByOutletCode("C60", dm);

            if(!isStockIn && qty > current) {
                System.out.println("Not enough stock.");
                continue;
            }

            // FIX: Pass 'dm' to setters
            if(isStockIn) p.setStockByOutletCode("C60", dm, current + qty);
            else p.setStockByOutletCode("C60", dm, current - qty);

            names.add(p.getModel());
            qtys.add(qty);
            totalQty += qty;
        }

        if(totalQty > 0) {
            dm.saveProducts();
            // Generate Receipt
            try (FileWriter fw = new FileWriter("../sales/receipts_" + LocalDate.now() + ".txt", true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("=== " + type + " ===");
                pw.println("Date: " + LocalDate.now() + " " + LocalTime.now());
                pw.println("By Employee: " + emp.getName()); // FIX: Uses getName()
                pw.println("Other Party: " + otherParty);
                for(int i=0; i<names.size(); i++) {
                    pw.println("- " + names.get(i) + ": " + qtys.get(i));
                }
                pw.println("--------------------------------");
                System.out.println("Receipt Saved.");
            } catch (IOException e) {
                System.out.println("Error saving receipt.");
            }
        }
    }

    // Removed viewStock method
}