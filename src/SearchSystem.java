import java.util.ArrayList;
import java.util.Scanner;


public class SearchInformation {

    public static void searchModel(ArrayList<Model> models, Scanner input) {

        ArrayList<String> models = new ArrayList<>();
        Scanner input = new Scanner(System.in); 

        System.out.print("Search Model Name: ");
        String kw = input.nextLine();

        boolean foundModel = false;

        for (Model mod : models) {
            if (mod.name.equalsIgnoreCase(kw)) {
                found = true;

                System.out.println("\nModel: " + mod.name);
                System.out.println("Unit Price: RM" + mod.price);
                System.out.println("Stock by Outlet:");
                System.out.println("Kuala Lumpur City Centre: " + mod.stock[0]
                        + "  MidValley: " + mod.stock[1]
                        + "  Sunway Velocity: " + mod.stock[2]
                        + "  IOI City Mall: " + mod.stock[3]
                        + "  Lalaport: " + mod.stock[4]
                        + "  Kuala Lumpur East Mall: " + mod.stock[5]
                        + "  NU Sentral: " + mod.stock[6]
                        + "  Pavilion Kuala Lumpur: " + mod.stock[7]
                        + "  Bukit Bintang: " + mod.stock[8]
                        + "  One Utama: " + mod.stock[9]
                        + "  MyTown: " + mod.stock[10]); );
                break;
            }
        }

        if (!found) {
            System.out.println("Model information not found.");
        }
    }

   
    public static void searchSales(ArrayList<Sale> sales, Scanner input) {

         ArrayList<String> sales = new ArrayList<>();
        Scanner input = new Scanner(System.in); 


        System.out.print("Search keyword (date/customer name /model name ): ");
        String kw = input.nextLine().toLowerCase();

        boolean found = false;

        for (Sale sal : sales) {
            if (sal.customer.toLowerCase().contains(kw)
                    || sal.model.toLowerCase().contains(kw)
                    || sal.date.contains(kw)) {

                found = true;

                System.out.println("\nSales Record Found:");
                System.out.println("Date: " + sal.date + "  Time: " + sal.time);
                System.out.println("Customer: " + sal.customer);
                System.out.println("Model: " + sal.model);
                System.out.println("Quantity: " + sal.quantity);
                System.out.println("Total: " +"RM" + sal.price);
                System.out.println("Transaction Method: " + sal.method);
                System.out.println("Employee: " + sal.employee);
                System.out.println("Status: Transaction verified.");
            }
        }

        if (!found) {
            System.out.println("Sales record not found.");
        }
    }
}










        )




