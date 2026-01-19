import java.io.*;
import java.util.*;

public class PerformanceMetrics {

    //store data for one employee
    static class StaffStats {
        String name;
        double totalSales;
        int transactionCount;

        public StaffStats(String name) {
            this.name = name;
            this.totalSales = 0;
            this.transactionCount = 0;
        }
    }

    public static void generateReport(Employee user) {
        // only managers can view this
        if (!user.getRole().equalsIgnoreCase("Manager")) { 
            System.out.println("\n>> ACCESS DENIED: Performance Metrics are for Managers only.");
            return;
        }

        System.out.println("\n=======================================================");
        System.out.println("           EMPLOYEE PERFORMANCE LEADERBOARD            ");
        System.out.println("=======================================================");
        
        File file = new File("../data/sales_history.csv");
        if (!file.exists()) {
            System.out.println(">> No sales history found. Make some sales first!");
            return;
        }

        //read file and calculate total
        Map<String, StaffStats> statsMap = new HashMap<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length < 5) continue; 

                String empName = data[1].trim(); 
                double saleTotal = Double.parseDouble(data[4].trim());

                //get record or create
                StaffStats stats = statsMap.getOrDefault(empName, new StaffStats(empName));
                
                stats.totalSales += saleTotal;
                stats.transactionCount++;
                
                statsMap.put(empName, stats);
            }
        } catch (Exception e) {
            System.out.println("Error processing data: " + e.getMessage());
            return;
        }

        //sort by Total Sales
        List<StaffStats> sortedList = new ArrayList<>(statsMap.values());
        sortedList.sort((s1, s2) -> Double.compare(s2.totalSales, s1.totalSales));

        //print table
        System.out.printf("%-5s | %-20s | %-15s | %-10s%n", "Rank", "Employee Name", "Total Sales", "Trans actions");
        System.out.println("------------------------------------------------------------");

        int rank = 1;
        for (StaffStats s : sortedList) {
            System.out.printf("#%-4d | %-20s | RM %-12.2f | %-10d%n", 
                rank++, s.name, s.totalSales, s.transactionCount);
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("End of Report.");
    }
}