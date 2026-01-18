public class Employee {
    private String employeeID;
    private String employeeName; // Variable is named 'employeeName'
    private String role;
    private String password;
    private Attendance attendance;

    public Employee(String employeeID, String employeeName, String role, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.password = password;
        this.attendance = new Attendance();
    }

    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    
    // Helper to extract outlet code from ID (e.g., C6001 -> C60)
    public String getOutlet() {
        if (employeeID.length() >= 3) return employeeID.substring(0, 3);
        return "HQ";
    }

    public Attendance getAttendance() { return attendance; }

    // Setters for Edit Functionality
    public void setPassword(String password) { this.password = password; }
    public void setEmployeeName(String name) { this.employeeName = name; }

    // === THE FIX IS HERE ===
    // This allows StockManagement to call emp.getName() without errors
    public String getName() {
        return this.employeeName; // Fixed: changed 'username' to 'employeeName'
    }
}