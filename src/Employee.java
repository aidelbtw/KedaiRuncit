public class Employee {
    private String employeeID;
    private String employeeName; 
    private String role;
    private String password;
    private Attendance attendance;

    
    private String currentSessionOutlet; 

    public Employee(String employeeID, String employeeName, String role, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.password = password;
        this.attendance = new Attendance();
        this.currentSessionOutlet = null; 
    }

    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    
  
    public String getOutlet() {
        
        if (this.currentSessionOutlet != null) {
            return this.currentSessionOutlet;
        }

        if (employeeID.length() >= 3) return employeeID.substring(0, 3);
        return "HQ";
    }

    public void setOutlet(String outlet) {
        this.currentSessionOutlet = outlet;
    }

    public Attendance getAttendance() { return attendance; }

    public void setPassword(String password) { this.password = password; }
    public void setEmployeeName(String name) { this.employeeName = name; }

    public String getName() {
        return this.employeeName; 
    }
}