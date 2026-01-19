import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AnalyticsSystem {
    
    private static class SalesRecord {
        LocalDate dateObj; 
        String empName;
        String custName;
        String model;
        int qty;
        double revenue;

        public SalesRecord(LocalDate d, String e, String c, String m, int q, double r) {
            this.dateObj = d; empName = e; custName = c; model = m; qty = q; revenue = r;
        }
    }

    private static class EmpStat {
        String name;
        double totalSales;
        int transactionCount;

        public EmpStat(String n) { this.name = n; }
    }

    //load data
    private static List<SalesRecord> loadSalesHistory() {
        List<SalesRecord> records = new ArrayList<>();
        File file = new File("../data/sales_history.csv");
        
        if (!file.exists()) return records; 

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 6) {
                    try {
                        records.add(new SalesRecord(
                            LocalDate.parse(parts[0]), parts[1], parts[2], parts[3], 
                            Integer.parseInt(parts[4]), Double.parseDouble(parts[5])
                        ));
                    } catch (Exception ex) {  }
                }
            }
        } catch (Exception e) { System.out.println("Error reading history."); }
        return records;
    }

    //main
    public static void showMenu(Employee user) {
        //check manager
        if (!user.getRole().equalsIgnoreCase("Manager")) {
            System.out.println("\n>> ACCESS DENIED: Analytics are for Managers only.");
            return;
        }

        Scanner input = new Scanner(System.in);
        
        while(true) {
            List<SalesRecord> data = loadSalesHistory();

            System.out.println("\n=== MANAGER ANALYTICS DASHBOARD ===");
            if(data.isEmpty()) System.out.println("[!] No sales history found.");
            
            System.out.println("1. Total Revenue Summary");
            System.out.println("2. Most Sold Product Model");
            System.out.println("3. Average Daily Revenue");
            System.out.println("4. Filter & Sort Transactions");
            System.out.println("5. Employee Performance Leaderboard"); // <--- ADDED BACK
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            
            int choice = -1;
            try { choice = Integer.parseInt(input.nextLine()); } catch (NumberFormatException e) {}

            if (choice == 6) return;
            if (data.isEmpty()) continue; 

            switch(choice) {
                case 1: //total revenue
                    double totalRev = 0;
                    for(SalesRecord r : data) totalRev += r.revenue;
                    System.out.printf("\n>> TOTAL REVENUE: RM %.2f\n", totalRev);
                    break;

                case 2: //best model
                    Map<String, Integer> counts = new HashMap<>();
                    for(SalesRecord r : data) counts.put(r.model, counts.getOrDefault(r.model, 0) + r.qty);
                    String best = "None"; int max = 0;
                    for(Map.Entry<String, Integer> e : counts.entrySet()) {
                        if(e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
                    }
                    System.out.println("\n>> MOST SOLD MODEL: " + best + " (" + max + " units)");
                    break;

                case 3: //avg daily
                    Map<String, Double> daily = new HashMap<>();
                    for(SalesRecord r : data) daily.put(r.dateObj.toString(), daily.getOrDefault(r.dateObj.toString(), 0.0) + r.revenue);
                    double sum = 0; for(double d : daily.values()) sum += d;
                    System.out.printf("\n>> AVG DAILY REVENUE: RM %.2f\n", (daily.isEmpty()?0 : sum/daily.size()));
                    break;

                case 4: //filter and sort
                    handleFilterAndSort(data, input);
                    break;
                
                case 5: //leaderboard
                    handleEmployeeLeaderboard(data);
                    break;
                    
                default: System.out.println("Invalid choice.");
            }
        }
    }


    private static void handleEmployeeLeaderboard(List<SalesRecord> records) {
        Map<String, EmpStat> statsMap = new HashMap<>();

        //aggregate data
        for (SalesRecord r : records) {
            statsMap.putIfAbsent(r.empName, new EmpStat(r.empName));
            EmpStat stats = statsMap.get(r.empName);
            stats.totalSales += r.revenue;
            stats.transactionCount++; //counting data
        }

        //sort descending
        List<EmpStat> sorted = new ArrayList<>(statsMap.values());
        sorted.sort((a, b) -> Double.compare(b.totalSales, a.totalSales));

        //display table
        System.out.println("\n--- EMPLOYEE PERFORMANCE LEADERBOARD ---");
        System.out.printf("%-5s | %-20s | %-15s | %-10s%n", "Rank", "Name", "Total Sales", "Trans actions");
        System.out.println("------------------------------------------------------------");

        int rank = 1;
        for (EmpStat s : sorted) {
            System.out.printf("#%-4d | %-20s | RM %-12.2f | %-10d%n", 
                rank++, s.name, s.totalSales, s.transactionCount);
        }
        System.out.println("------------------------------------------------------------");
    }

    //filter sort
    private static void handleFilterAndSort(List<SalesRecord> records, Scanner input) {
        System.out.println("\n--- Filter by Date Range ---");
        System.out.print("Enter Start Date (YYYY-MM-DD) or 'all': ");
        String startStr = input.nextLine();

        List<SalesRecord> filtered = new ArrayList<>();

        if (startStr.equalsIgnoreCase("all")) {
            filtered.addAll(records);
        } else {
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String endStr = input.nextLine();
            try {
                LocalDate start = LocalDate.parse(startStr);
                LocalDate end = LocalDate.parse(endStr);
                for (SalesRecord r : records) {
                    if ((r.dateObj.isEqual(start) || r.dateObj.isAfter(start)) && 
                        (r.dateObj.isEqual(end) || r.dateObj.isBefore(end))) {
                        filtered.add(r);
                    }
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Showing all.");
                filtered.addAll(records);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\n--- Sort Results ---");
        System.out.println("1. Date (Newest)");
        System.out.println("2. Amount (Highest)");
        System.out.println("3. Amount (Lowest)");
        System.out.println("4. Customer Name (A-Z)");
        System.out.print("Choice: ");
        int s = -1; try{s=Integer.parseInt(input.nextLine());}catch(Exception e){}

        if(s==1) filtered.sort((a, b) -> b.dateObj.compareTo(a.dateObj));
        else if(s==2) filtered.sort((a, b) -> Double.compare(b.revenue, a.revenue));
        else if(s==3) filtered.sort((a, b) -> Double.compare(a.revenue, b.revenue));
        else if(s==4) filtered.sort((a, b) -> a.custName.compareToIgnoreCase(b.custName));

        System.out.println("\n------------------------------------------------------------------");
        System.out.printf("%-12s | %-15s | %-15s | %8s\n", "Date", "Customer", "Model", "Total");
        System.out.println("------------------------------------------------------------------");
        for (SalesRecord r : filtered) {
            System.out.printf("%-12s | %-15s | %-15s | %8.2f\n", r.dateObj, r.custName, r.model, r.revenue);
        }
    }
}