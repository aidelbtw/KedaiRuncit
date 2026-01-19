public class Employee {
    private String employeeID;
    private String employeeName; 
    private String role;
    private String password;
    private Attendance attendance;

    // === NEW VARIABLE ===
    // This holds the shop they selected during Login.
    // It is temporary and exists only while the app is running.
    private String currentSessionOutlet; 

    public Employee(String employeeID, String employeeName, String role, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.password = password;
        this.attendance = new Attendance();
        this.currentSessionOutlet = null; // Default is null until login
    }

    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    
    // === UPDATED LOGIC ===
    public String getOutlet() {
        // Priority 1: Return the outlet they chose at Login screen
        if (this.currentSessionOutlet != null) {
            return this.currentSessionOutlet;
        }

        // Priority 2: Fallback (extract from ID if they haven't logged in yet)
        if (employeeID.length() >= 3) return employeeID.substring(0, 3);
        return "HQ";
    }

    // === NEW SETTER ===
    // This is called by LoginSystem.java when the user picks a shop
    public void setOutlet(String outlet) {
        this.currentSessionOutlet = outlet;
    }

    public Attendance getAttendance() { return attendance; }

    // Setters for Edit Functionality
    public void setPassword(String password) { this.password = password; }
    public void setEmployeeName(String name) { this.employeeName = name; }

    // Helper for StockManagement
    public String getName() {
        return this.employeeName; 
    }
}