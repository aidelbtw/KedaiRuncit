import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

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
            System.out.println("4. Filter and Sort Sales History");
            System.out.println("5. Back");
            System.out.print("Choice: ");
            int ch = input.nextInt(); input.nextLine();

            switch(ch) {
                case 1:
                    double total = 0;
                    for(String[] row : data) total += Double.parseDouble(row[2]);
                    System.out.println("Total Revenue: RM" + String.format("%.2f", total));
                    break; 
                case 2:
                    System.out.println("--- High Value Transactions ---");
                    for(String[] row : data) {
                        if(Double.parseDouble(row[2]) > 1000) {
                            System.out.println("Date: " + row[0] + " | Amount: RM" + row[2]);
                        }
                    }
                    break;
                case 3:
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
                    break;
                case 4:
                    filterAndSortSalesHistory(data);
                    break;
                default:
                    return;
            }
        }
    }

    private static void filterAndSortSalesHistory(List<String[]> data) {
        Scanner input = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Filter by date range
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = input.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = input.nextLine();

        LocalDate startDate, endDate;
        try {
            startDate = LocalDate.parse(startDateStr, formatter);
            endDate = LocalDate.parse(endDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        List<String[]> filteredData = data.stream()
            .filter(row -> {
                LocalDate saleDate = LocalDate.parse(row[0], formatter);
                return !saleDate.isBefore(startDate) && !saleDate.isAfter(endDate);
            })
            .collect(Collectors.toList());

        if(filteredData.isEmpty()) {
            System.out.println("No sales data found within the specified date range.");
            return;
        }

        // Display cumulative sales
        double cumulativeSales = filteredData.stream()
            .mapToDouble(row -> Double.parseDouble(row[2]))
            .sum();
        System.out.println("Cumulative Sales for " + startDateStr + " to " + endDateStr + ": RM" + String.format("%.2f", cumulativeSales));

        // Sorting options
        System.out.println("\nChoose sorting criteria:");
        System.out.println("1. Date (Ascending)");
        System.out.println("2. Date (Descending)");
        System.out.println("3. Amount (Lowest to Highest)");
        System.out.println("4. Amount (Highest to Lowest)");
        System.out.println("5. Customer Name (Alphabetically)");
        System.out.print("Choice: ");
        int sortChoice = input.nextInt(); input.nextLine();

        Comparator<String[]> comparator = null;
        switch(sortChoice) {
            case 1:
                comparator = Comparator.comparing(row -> LocalDate.parse(row[0], formatter));
                break;
            case 2:
                comparator = Comparator.comparing(row -> LocalDate.parse(row[0], formatter)).reversed();
                break;
            case 3:
                comparator = Comparator.comparingDouble(row -> Double.parseDouble(row[2]));
                break;
            case 4:
                comparator = Comparator.comparingDouble(row -> Double.parseDouble(row[2])).reversed();
                break;
            case 5:
                comparator = Comparator.comparing(row -> row[1]);
                break;
            default:
                System.out.println("Invalid choice. No sorting applied.");
                return;
        }

        List<String[]> sortedData = filteredData.stream()
            .sorted(comparator)
            .collect(Collectors.toList());

        // Tabular display
        System.out.println("\nFiltered and Sorted Sales Records:");
        System.out.printf("%-15s %-20s %-10s %-15s %-10s\n", "Date", "Customer", "Amount", "Employee ID", "Outlet");
        for(String[] row : sortedData) {
            System.out.printf("%-15s %-20s RM%-9.2f %-15s %-10s\n", row[0], row[1], Double.parseDouble(row[2]), row[3], row[4]);
        }
    }
}
