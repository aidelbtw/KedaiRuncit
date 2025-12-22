import java.util.Scanner;
import java.util.List;
public class LoginSystem {
    public static Employee login(List<Employee> employees) {
        Scanner input = new Scanner(System.in);

        System.out.print("Employee ID: ");
        String id = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        //After user inputs, system will search thru data of employee.csv to find a match and login, if not will fail
        for(Employee staff : employees){
            if(staff.getEmployeeID().equals(id) && staff.getPassword().equals(password)){
                System.out.println("Login Sucessful!");
                System.out.println("Welcome, " + staff.getEmployeeName());
                return staff;
            }
        }
        System.out.println("Login Failed: Invalid User ID or Password.");
        return null;
    }
}
