import java.io.*;
import java.util.*;

public class AnalyticsSystem {
    
    // Helper class to store row data
    private static class SalesRecord {
        String date;
        String model;
        int qty;
        double revenue;

        public SalesRecord(String date, String model, int qty, double revenue) {
            this.date = date;
            this.model = model;
            this.qty = qty;
            this.revenue = revenue;
        }
    }

    private static List<SalesRecord> loadSalesHistory() {
        List<SalesRecord> records = new ArrayList<>();
        File file = new File("../data/sales_history.csv");
        
        if (!file.exists()) return records; // Return empty list if no file

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String date = parts[0];
                    String model = parts[1];
                    int qty = Integer.parseInt(parts[2]);
                    double revenue = Double.parseDouble(parts[3]);
                    records.add(new SalesRecord(date, model, qty, revenue));
                }
            }
        } catch (Exception e) { 
            System.out.println("Error reading sales history."); 
        }
        return records;
    }

    public static void showMenu() {
        Scanner input = new Scanner(System.in);
        
        while(true) {
            List<SalesRecord> data = loadSalesHistory();

            System.out.println("\n=== SALES ANALYTICS ===");
            if(data.isEmpty()) {
                System.out.println("[!] No sales history found yet.");
                System.out.println("    (Make a sale in Sales System first)");
            }
            
            System.out.println("1. Total Revenue");
            System.out.println("2. Most Sold Product Model");
            System.out.println("3. Average Daily Revenue");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            
            int choice = -1;
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {}

            if (choice == 4) return;

            if (data.isEmpty()) continue; // Skip logic if no data

            switch(choice) {
                case 1:
                    double totalRev = 0;
                    for(SalesRecord r : data) totalRev += r.revenue;
                    System.out.printf("\n>> TOTAL REVENUE: RM %.2f\n", totalRev);
                    break;

                case 2:
                    Map<String, Integer> counts = new HashMap<>();
                    for(SalesRecord r : data) {
                        counts.put(r.model, counts.getOrDefault(r.model, 0) + r.qty);
                    }
                    
                    String bestModel = "None";
                    int maxQty = 0;
                    
                    for(Map.Entry<String, Integer> entry : counts.entrySet()) {
                        if(entry.getValue() > maxQty) {
                            maxQty = entry.getValue();
                            bestModel = entry.getKey();
                        }
                    }
                    System.out.println("\n>> MOST SOLD MODEL: " + bestModel + " (" + maxQty + " units)");
                    break;

                case 3:
                    // Group revenue by date
                    Map<String, Double> dailyRev = new HashMap<>();
                    for(SalesRecord r : data) {
                        dailyRev.put(r.date, dailyRev.getOrDefault(r.date, 0.0) + r.revenue);
                    }
                    
                    double totalDaily = 0;
                    for(double d : dailyRev.values()) totalDaily += d;
                    
                    double avg = dailyRev.isEmpty() ? 0 : totalDaily / dailyRev.size();
                    
                    System.out.printf("\n>> AVERAGE DAILY REVENUE: RM %.2f (over %d active days)\n", avg, dailyRev.size());
                    break;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}