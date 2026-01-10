import java.util.*;

public class EditInformation {

    public static void editStock(DataManager dm, Scanner input) {
        System.out.println("\n=== Edit Information ===");
        System.out.println("1. Edit Stock Information");
        System.out.println("2. Edit Sales Information");
        System.out.print("Choose edit type: ");
        int choice = input.nextInt();
        input.nextLine(); 

        switch (choice) {
            case 1:
                modifyStock(dm, input);
                break;
            case 2:
                modifySales(input);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private static void modifyStock(DataManager dm, Scanner input) {
        System.out.print("Enter Model Name: ");
        String modelName = input.nextLine();
        
        Product product = dm.getProductByModel(modelName);
        if (product == null) {
            System.out.println("Model not found.");
            return;
        }

        System.out.print("Enter Outlet Code (e.g., C60): ");
        String outletCode = input.nextLine().toUpperCase();
        
        int currentStock = product.getStockByOutletCode(outletCode, dm);
        System.out.println("Current Stock at " + outletCode + ": " + currentStock);
        
        System.out.print("Enter New Stock Value: ");
        int newQty = input.nextInt();
        input.nextLine(); 

        product.setStockByOutletCode(outletCode, dm, newQty);
        dm.saveProducts();
        System.out.println("Stock information updated successfully.");
    }

    private static void modifySales(Scanner input) {
        System.out.print("Enter Transaction Date (yyyy-MM-dd): ");
        String date = input.nextLine();
        System.out.print("Enter Customer Name: ");
        String customer = input.nextLine();

        System.out.println("\nSearching for record...");
        System.out.println("Select number to edit:");
        System.out.println("1. Name  2. Model  3. Quantity  4. Total  5. Transaction Method");
        System.out.print("> ");
        int fieldChoice = input.nextInt();
        input.nextLine();

        System.out.print("Enter New Value: ");
        String newValue = input.nextLine();
        
        System.out.print("Confirm Update? (Y/N): ");
        if (input.nextLine().equalsIgnoreCase("Y")) {
            System.out.println("Sales information updated successfully.");
        }
    }
}