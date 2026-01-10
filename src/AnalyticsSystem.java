import java.io.*;
import java.util.*;

public class AnalyticsSystem {
    
    private static List<String[]> loadSalesLog() {
        List<String[]> logs = new ArrayList<>();
        // PATH: ../data/sales_history.csv
        try (BufferedReader br = new BufferedReader(new FileReader("../data/sales_history.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(line.split(","));
            }
        } catch (IOException e) { System.out.println("No sales history found."); }
        return logs;
    }

    public static void showMenu() {
        Scanner input = new Scanner(System.in);
        List<String[]> data = loadSalesLog();
        if(data.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        while(true) {
            System.out.println("\n=== EXTRA FEATURES: Analytics ===");
            System.out.println("1. Total Revenue");
            System.out.println("2. Filter Sales > RM1000");
            System.out.println("3. Employee Performance");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            int ch = input.nextInt(); input.nextLine();

            if(ch == 1) {
                double total = 0;
                for(String[] row : data) total += Double.parseDouble(row[2]);
                System.out.println("Total Revenue: RM" + String.format("%.2f", total));
            } 
            else if(ch == 2) {
                System.out.println("--- High Value Transactions ---");
                for(String[] row : data) {
                    if(Double.parseDouble(row[2]) > 1000) {
                        System.out.println("Date: " + row[0] + " | Amount: RM" + row[2]);
                    }
                }
            }
            else if(ch == 3) {
                System.out.println("--- Sales by Employee ---");
                Map<String, Double> performance = new HashMap<>();
                for(String[] row : data) {
                    String empID = row[3];
                    double amt = Double.parseDouble(row[2]);
                    performance.put(empID, performance.getOrDefault(empID, 0.0) + amt);
                }
                for(String id : performance.keySet()) {
                    System.out.println("Staff " + id + ": RM" + String.format("%.2f", performance.get(id)));
                }
            }
            else break;
        }
    }
}
