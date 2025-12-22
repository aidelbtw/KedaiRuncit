public class Attendance {
    public static void record(Employee staff, DataManager dm){
        System.out.println("===== Attendance Clock In =====");
        System.out.println("Name: " + staff.getEmployeeName());

        // Get outlet code from employee
        String outletCode = staff.getOutlet();

        // Get outlet name from DataManager
        String outletName = dm.getOutletName(outletCode);

        System.out.println("Outlet: " + outletName);
    }
}
