import java.util.*;

public class LoginSystem {
    public static Employee login(List<Employee> employees) {
        Scanner input = new Scanner(System.in);
        System.out.print("Employee ID: ");
        String id = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();

        for(Employee e : employees){
            if(e.getEmployeeID().equalsIgnoreCase(id) && e.getPassword().equals(pass)){
                System.out.println("Login Successful! Welcome, " + e.getEmployeeName());
                return e;
            }
        }
        System.out.println("Invalid Credentials.");
        return null;
    }

    public static void registerEmployee(DataManager dm) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n=== Employee Registration ===");
        System.out.print("Enter New ID (e.g. C6099): ");
        String id = input.nextLine();
        
        // Check duplicate
        for(Employee e : dm.getEmployees()) {
            if(e.getEmployeeID().equalsIgnoreCase(id)) {
                System.out.println("ID already exists!");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = input.nextLine();
        System.out.print("Enter Role (Manager/Full-time/Part-time): ");
        String role = input.nextLine();
        System.out.print("Enter Password: ");
        String pass = input.nextLine();

        Employee newEmp = new Employee(id, name, role, pass);
        dm.getEmployees().add(newEmp);
        dm.saveEmployees();
        System.out.println("Employee registered successfully.");
    }
}
