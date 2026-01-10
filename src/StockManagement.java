import java.util.Scanner;
public class StockManagement {
    public static void manage(DataManager dm, Employee user) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n=== Stock Management ===");
        System.out.println("1. View/Verify Stock");
        System.out.println("2. Add Stock (Stock In)");
        System.out.print("Choice: ");
        int ch = input.nextInt(); input.nextLine();

        String outlet = user.getOutlet();
        if(outlet.length() < 3) { // If HQ, ask which outlet
             System.out.print("Enter Outlet Code: ");
             outlet = input.nextLine().toUpperCase();
        }

        if(ch == 1) {
            for(Product p : dm.getProducts()) {
                int qty = p.getStockByOutletCode(outlet, dm);
                if(qty > 0) System.out.println(p.getModel() + ": " + qty);
            }
        } else if (ch == 2) {
            System.out.print("Model: ");
            Product p = dm.getProductByModel(input.nextLine());
            if(p != null) {
                System.out.print("Quantity to add: ");
                int add = input.nextInt();
                int current = p.getStockByOutletCode(outlet, dm);
                p.setStockByOutletCode(outlet, dm, current + add);
                dm.saveProducts();
                System.out.println("Stock updated.");
            }
        }
    }
}
