import java.util.*;

public class LoginSystem {
    
    //list of valid outlet
    private static final List<String> VALID_OUTLETS = Arrays.asList(
        "C60", "C61", "C62", "C63", "C64", "C65", "C66", "C67", "C68", "C69", "HQ"
    );

    public static Employee login(List<Employee> employees) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String id = input.nextLine();
        System.out.print("Enter Password: ");
        String pass = input.nextLine();

        for(Employee e : employees){
            if(e.getEmployeeID().equalsIgnoreCase(id) && e.getPassword().equals(pass)){
                System.out.println(">> Password Accepted.");

                while (true) {
                    System.out.println("\nWhere are you working today?");
                    System.out.println("Valid Outlets: " + VALID_OUTLETS);
                    System.out.print("Enter Outlet Code: ");
                    String outletInput = input.nextLine().toUpperCase().trim();

                    if (VALID_OUTLETS.contains(outletInput)) {
                        // Set the session outlet for this user
                        e.setOutlet(outletInput); 
                        
                        System.out.println("Login Successful!");
                        System.out.println("Welcome, " + e.getEmployeeName() + " (Session: " + e.getOutlet() + ")");
                        return e;
                    } else {
                        System.out.println(">> Invalid Outlet. Please try again.");
                    }
                }
            }
        }
        System.out.println("Login Failed: Invalid User ID or Password");
        return null;
    }

    public static void registerEmployee(DataManager dm) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n=== Employee Registration ===");
        System.out.print("Enter Employee ID (e.g. C6099): ");
        String id = input.nextLine();
        
        // check duplicate
        for(Employee e : dm.getEmployees()) {
            if(e.getEmployeeID().equalsIgnoreCase(id)) {
                System.out.println("ID already exists!");
                return;
            }
        }

        System.out.print("Enter Employee Name: ");
        String name = input.nextLine();
        System.out.print("Set Role (Manager/Full-time/Part-time): ");
        String role = input.nextLine();
        System.out.print("Set Password: ");
        String pass = input.nextLine();

        Employee newEmp = new Employee(id, name, role, pass);
        dm.getEmployees().add(newEmp);
        dm.saveEmployees();
        System.out.println("Employee successfully registered!");
    }
}