import java.io.*;
import java.util.*;

public class EditInformation {

    public static void editStock(DataManager dm, Scanner input) {
        System.out.println("\n=== Edit Information ===");
        System.out.println("1. Edit Stock Information");
        System.out.println("2. Edit Sales Information");
        System.out.print("Choose: ");
        int choice = input.nextInt();
        input.nextLine(); 

        if (choice == 1) {
            modifyStock(dm, input);
        } else if (choice == 2) {
            modifySalesInTxt(input);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void modifyStock(DataManager dm, Scanner input) {
        System.out.print("Enter Model Name: ");
        String modelName = input.nextLine();
        Product product = dm.getProductByModel(modelName);
        if (product != null) {
            System.out.print("Enter Outlet Code (e.g., C60): ");
            String outletCode = input.nextLine().toUpperCase();
            int currentQty = product.getStockByOutletCode(outletCode, dm);
            System.out.println("Current Stock at " + dm.getOutletName(outletCode) + ": " + currentQty);
            System.out.print("Enter New Stock Value: ");
            int newQty = input.nextInt();
            input.nextLine(); 
            product.setStockByOutletCode(outletCode, dm, newQty);
            dm.saveProducts(); 
            System.out.println("Stock information updated successfully.");
        } else {
            System.out.println("Model not found.");
        }
    }

    private static void modifySalesInTxt(Scanner input) {
        System.out.print("Enter Transaction Date (yyyy-MM-dd): ");
        String date = input.nextLine();
        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();

        File file = new File("sales/sales_" + date + ".txt");

        if (!file.exists()) {
            System.out.println("No sales record file found for date: " + date);
            return;
        }

        List<String> allLines = new ArrayList<>();
        List<String> currentRecord = new ArrayList<>();
        boolean foundRecord = false;
        boolean isTargetRecord = false;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                currentRecord.add(line);

                if (line.contains("Customer Name: " + customerName)) {
                    isTargetRecord = true;
                }

                if (line.contains("=====================================")) {
                    if (isTargetRecord && !foundRecord) {
                        foundRecord = true;
                        processEdit(currentRecord, input);
                    }
                    allLines.addAll(currentRecord);
                    currentRecord.clear();
                    isTargetRecord = false;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
            return;
        }

        if (foundRecord) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String line : allLines) {
                    pw.println(line);
                }
                System.out.println("Sales receipt updated successfully.");
            } catch (IOException e) {
                System.out.println("Error saving updates to file.");
            }
        } else {
            System.out.println("Specific transaction for " + customerName + " not found.");
        }
    }

    private static void processEdit(List<String> record, Scanner input) {
        System.out.println("\nSelect number to edit:");
        System.out.println("1. Customer Name");
        System.out.println("2. Model (Item)");
        System.out.println("3. Quantity");
        System.out.println("4. Transaction Method");
        System.out.println("5. Total Price");
        int choice = input.nextInt();
        input.nextLine();
        System.out.print("Enter New Value: ");
        String newValue = input.nextLine();

        String targetPrefix = switch (choice) {
            case 1 -> "Customer Name: ";
            case 2 -> "Item(s): ";
            case 3 -> "Quantity: ";
            case 4 -> "Transaction Method: ";
            case 5 -> "Total Price: RM";
            default -> "";
        };

        for (int i = 0; i < record.size(); i++) {
            if (record.get(i).startsWith(targetPrefix)) {
                // Special handling for Model/Quantity if they are on the same line in your TXT format
                if (choice == 2 || choice == 3) {
                    // This logic assumes the line format "Item(s): [Name]   Quantity: [Num]"
                    String currentLine = record.get(i);
                    if (choice == 2) {
                        record.set(i, "Item(s): " + newValue + currentLine.substring(currentLine.indexOf("   Quantity:")));
                    } else {
                        record.set(i, currentLine.substring(0, currentLine.indexOf("Quantity:")) + "Quantity: " + newValue);
                    }
                } else {
                    record.set(i, targetPrefix + newValue);
                }
                break;
            }
        }
    }
}
