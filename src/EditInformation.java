import java.io.*;
import java.util.*;

public class EditInformation {

    public static void editStock(DataManager dm, Employee user) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n=== Edit Information ===");
        System.out.println("Logged in as: " + user.getEmployeeName());
        System.out.println("1. Edit Stock Information");
        System.out.println("2. Edit Sales Information");
        System.out.print("Choice: ");
        
        int choice = -1;
        try { choice = input.nextInt(); } catch(Exception e) { }
        input.nextLine(); 

        if (choice == 1) {
            modifyStock(dm, input);
        } else if (choice == 2) {
            modifySales(input);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void modifyStock(DataManager dm, Scanner input) {
        System.out.print("Enter Model Name: ");
        String modelName = input.nextLine();
        Product product = dm.getProductByModel(modelName);

        if (product != null) {
            System.out.print("Enter Outlet Code (e.g., C60): ");
            String code = input.nextLine().toUpperCase();
            int idx = dm.getOutletIndex(code);

            if (idx != -1) {
                System.out.println("Current Stock at " + dm.getOutletName(code) + ": " + product.getStockByOutletIndex(idx));
                System.out.print("New Stock Value: ");
                int newQty = input.nextInt();
                input.nextLine();

                product.getStockLevels()[idx] = newQty;
                dm.saveProducts();
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("Invalid Outlet Code.");
            }
        } else {
            System.out.println("Model not found.");
        }
    }

    private static void modifySales(Scanner input) {
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = input.nextLine();
        System.out.print("Enter Customer Name: ");
        String customer = input.nextLine();

        File file = new File("sales/sales_" + date + ".txt");
        if (!file.exists()) {
            System.out.println("No record file found for that date.");
            return;
        }

        List<String> allLines = new ArrayList<>();
        List<String> currentRecord = new ArrayList<>();
        boolean found = false;
        boolean target = false;

        try (Scanner fs = new Scanner(file)) {
            while (fs.hasNextLine()) {
                String line = fs.nextLine();
                currentRecord.add(line);
                if (line.contains("Customer Name: " + customer)) target = true;
                if (line.contains("=====================================")) {
                    if (target && !found) {
                        found = true;
                        applyEdit(currentRecord, input);
                    }
                    allLines.addAll(currentRecord);
                    currentRecord.clear();
                    target = false;
                }
            }
        } catch (Exception e) { return; }

        if (found) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : allLines) pw.println(l);
                System.out.println("Sales receipt updated permanently.");
            } catch (Exception e) { System.out.println("Error saving file."); }
        } else {
            System.out.println("Transaction not found.");
        }
    }

    private static void applyEdit(List<String> record, Scanner input) {
        System.out.println("\nSelect number to edit:");
        System.out.println("1. Customer Name 2. Model 3. Quantity 4. Method 5. Total Price");
        int choice = input.nextInt(); input.nextLine();
        System.out.print("Enter New Value: ");
        String val = input.nextLine();

        String prefix = switch (choice) {
            case 1 -> "Customer Name: ";
            case 2 -> "Item(s): ";
            case 3 -> "Quantity: ";
            case 4 -> "Transaction Method: ";
            case 5 -> "Total Price: RM";
            default -> "";
        };

        for (int i = 0; i < record.size(); i++) {
            if (record.get(i).startsWith(prefix)) {
                if ((choice == 2 || choice == 3) && record.get(i).contains("Quantity:")) {
                    String line = record.get(i);
                    if (choice == 2) record.set(i, "Item(s): " + val + line.substring(line.indexOf("   Quantity:")));
                    else record.set(i, line.substring(0, line.indexOf("Quantity:")) + "Quantity: " + val);
                } else {
                    record.set(i, prefix + val);
                }
                break;
            }
        }
    }
}
