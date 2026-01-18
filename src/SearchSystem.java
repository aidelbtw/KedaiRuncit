import java.io.*;
import java.util.*;

public class SearchSystem {

    // === CHANGED NAME FROM searchModel TO search ===
    public static void search(DataManager dm) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n=== Search Information ===");
        System.out.println("1. Stock Information");
        System.out.println("2. Sales Information");
        System.out.print("Choice: ");
        
        int choice = -1;
        try { choice = input.nextInt(); } catch(Exception e) { }
        input.nextLine(); 

        if (choice == 1) {
            searchStock(dm, input);
        } else if (choice == 2) {
            searchSales(input);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void searchStock(DataManager dm, Scanner input) {
        System.out.print("Search Model Name: ");
        String modelName = input.nextLine();
        
        // Ensure getProductByModel exists in DataManager or use getProduct
        Product product = dm.getProduct(modelName); 
        
        if (product == null) {
            System.out.println("Searching...\nModel not found.");
            return;
        }

        System.out.println("Searching...");
        System.out.println("Model: " + product.getModel());
        System.out.println("Unit Price: RM" + String.format("%.2f", product.getPrice()));
        System.out.println("Stock by Outlet:");

        List<String> codes = dm.getOutletCodes();
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i);
            String name = dm.getOutletName(code);
            int qty = product.getStockByOutletIndex(i);
            System.out.printf("%s: %d  ", name, qty);
            if ((i + 1) % 2 == 0) System.out.println();
        }
        System.out.println();
    }

    private static void searchSales(Scanner input) {
        System.out.print("Search keyword (Date/Customer/Model): ");
        String keyword = input.nextLine().toLowerCase();
        
        File folder = new File("../sales/"); 
        File[] salesFiles = folder.listFiles((dir, name) -> name.startsWith("sales_") && name.endsWith(".txt"));

        if (salesFiles == null || salesFiles.length == 0) {
            System.out.println("No sales records found.");
            return;
        }

        System.out.println("Searching...");
        boolean found = false;

        for (File file : salesFiles) {
            try (Scanner fileScanner = new Scanner(file)) {
                StringBuilder currentRecord = new StringBuilder();
                boolean matchFound = false;

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    currentRecord.append(line).append("\n");

                    if (line.toLowerCase().contains(keyword)) matchFound = true;

                    if (line.contains("=========================================")) {
                        if (matchFound) {
                            System.out.println("Sales Record Found:");
                            System.out.println(currentRecord.toString());
                            found = true;
                        }
                        currentRecord.setLength(0);
                        matchFound = false;
                    }
                }
            } catch (Exception e) { }
        }
        if (!found) System.out.println("No matching sales records found.");
    }
}