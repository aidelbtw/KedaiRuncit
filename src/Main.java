public class Main {
    public static void main(String[] args) {
        DataManager dm = new DataManager();
        // Use full path to your CSV
        dm.loadEmployees("C:\\Users\\User\\Desktop\\FOP Project\\KedaiRuncit\\data\\employees.csv");

        System.out.println("Loaded Employees: " + dm.getEmployees().size());

        System.out.println("EmployeeID EmployeeName Role");
        for (Employee e : dm.getEmployees()) {
            System.out.println(e.getEmployeeID() + " " + e.getEmployeeName() + " " + e.getRole());
        }
    }
}
