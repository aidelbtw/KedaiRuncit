import java.util.Scanner;

public class Main_ChangeOnlyYourPart {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //firstly will load all the data from DataManager
        DataManager dm = new DataManager();

        // Since data folder is inside src folder
        dm.loadEmployees("data/employee.csv");
        dm.loadOutlets("data/outlet.csv");
        dm.loadProducts("data/model.csv");
        
        // Debug: Print loaded data
        System.out.println("Loaded Employees: " + dm.getEmployees().size());
        System.out.println("Loaded Outlets: " + dm.getOutletCodes().size());
        System.out.println("Loaded Products: " + dm.getProducts().size());
        
        if (dm.getEmployees().size() == 0) {
            System.out.println("No employees loaded. Please check file paths and file contents.");
            return;
        }
        
        Employee currentEmployee = null;
        boolean go = true;

        while (go){
            System.out.println("\n===== Welcome to Kedai Runcit System =====");
            System.out.println("1. Log in");
            System.out.println("2. Shutdown");
            System.out.print("Enter your choice: ");
            int pick = input.nextInt();
            input.nextLine();
            
            switch (pick) {
                case 1:
                    //This will bring to LoginSystem to ask user for input of ID and 
                    currentEmployee = LoginSystem.login(dm.getEmployees());
                    if(currentEmployee == null) {
                        System.out.println("Login failed. Returning to main menu.");
                        continue; // Continue the main loop instead of returning
                    }

                    boolean running = true;

                    while (running) {
                        System.out.println("\n====== Welcome " + currentEmployee.getEmployeeName() + " to Kedai Runcit ======");
                        System.out.println("1. Attendance");
                        System.out.println("2. Stock Management");
                        System.out.println("3. Sales");
                        System.out.println("4. Search");
                        System.out.println("5. Edit Information");
                        System.out.println("6. Logout");
                        System.out.print("Choose: ");
                        int choice = input.nextInt();
                        input.nextLine();

                        switch (choice) {
                            case 1:
                                AttendanceMenu.menu(currentEmployee, dm, currentEmployee.getAttendance());
                                break;
                            case 2:
                                StockManagement.manage(dm, currentEmployee);
                                break;
                            case 3:
                               SalesSystem.sell(dm, currentEmployee);
                                break;
                            case 4:
                               // SearchSystem.search();
                                break;
                            case 5:
                                //EditSystem.edit();
                                break;
                            case 6:
                                System.out.println("Logged out");
                                running = false;
                                break;
                            default:
                                System.out.println("Invalid option");
                        }            
                    }
                    break;
                case 2:
                    go = false;
                    System.out.println("System shutdown...");
                    return;
                default:
                    System.out.println("Invalid Option");
            }
        }
    }
}