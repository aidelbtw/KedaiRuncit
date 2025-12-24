import java.util.Scanner;
public class Main_ChangeOnlyYourPart {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //firstly will load all the data from DataManager
        DataManager dm = new DataManager();

        // Use full path to your CSV
        dm.loadEmployees("data/employee.csv");
        dm.loadOutlets("data/outlet.csv");
        
        Employee currentEmployee = null;
        boolean go = true;

        while (go){
            System.out.println("===== Welcome to Kedai Runcit System =====");
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
                    return;
                    }

                    boolean running = true;

        while (running) {
            System.out.println("====== Welcome " + currentEmployee.getEmployeeName() + " to Kedai Runcit ======");
            System.out.println("1. Attendace");
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
                    //StockSystem.manage();
                    break;
                case 3:
                   // SalesSystem.sell();
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
            }            
        }
                break;
                case 2:
                    go = false;
                    return;
                default:
                    System.out.println("Invalid Option");
        }
        //System.out.println("Loaded Employees: " + dm.getEmployees().size());
        //System.out.println("EmployeeID EmployeeName Role");
        //for (Employee e : dm.getEmployees()) {
        //    System.out.println(e.getEmployeeID() + " " + e.getEmployeeName() + " " + e.getRole());
        //}    
    }

}
}
