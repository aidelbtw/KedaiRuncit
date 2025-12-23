import java.util.Scanner;
public class Main_ChangeOnlyYourPart {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //firstly will load all the data from DataManager
        DataManager dm = new DataManager();

        // Use full path to your CSV
        dm.loadEmployees("data/employee.csv");
        dm.loadOutlets("data/outlet.csv");
        
        //This will bring to LoginSystem to ask user for input of ID and 
        Employee currentEmployee = LoginSystem.login(dm.getEmployees());
        if(currentEmployee == null) {
            input.close();
            return;
        }

        Attendance attendance = new Attendance(); // create one attendance per session
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
                AttendanceMenu.menu(currentEmployee, dm, attendance);
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
        System.out.println("Loaded Employees: " + dm.getEmployees().size());

        System.out.println("EmployeeID EmployeeName Role");
        for (Employee e : dm.getEmployees()) {
            System.out.println(e.getEmployeeID() + " " + e.getEmployeeName() + " " + e.getRole());
        }
        input.close();
    }
}
