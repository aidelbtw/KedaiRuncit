public class Employee {
    private String employeeID;
    private String employeeName;
    private String role;
    private String password;

    // Constructor
    public Employee(String employeeID, String employeeName, String role, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.password = password;
    }

    // Getters
    public String getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getOutlet(){
        return employeeID.substring(0, 3);
    }
}
