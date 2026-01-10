import java.util.*;

public class LoginSystem {
    public static Employee login(List<Employee> employees) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String id = input.nextLine();
        System.out.print("Enter Password: ");
        String pass = input.nextLine();

        for(Employee e : employees){
            if(e.getEmployeeID().equalsIgnoreCase(id) && e.getPassword().equals(pass)){
                System.out.println("Login Successful!");
                System.out.println("Welcome, " + e.getEmployeeName() + " (" + e.getOutlet() + ")");
                return e;
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
        
        // Check duplicate
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
