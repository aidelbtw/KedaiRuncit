import java.time.*;
import java.time.format.DateTimeFormatter;
public class Attendance {

    private boolean isClockedIn = false;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    private String timeFormat(LocalDateTime time){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm a");
        String formatted = time.format(fmt);
        return formatted;
    }

    //This one is to connect with the main to see whether clocked in or out
    public  boolean hasClockedIn(){
        return clockInTime != null;
    }

    public void clockIn(Employee staff, DataManager dm){
        if(isClockedIn){
            System.out.println("You are already clocked in!");
            return;
        }
        
        // to get. the outlet code from Employee.java and name from DataManager.java using code
        String outletCode = staff.getOutlet();
        String outletName = dm.getOutletName(outletCode);

        clockInTime = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        isClockedIn = true;

        System.out.println("===== Attendance Clock In =====");
        System.out.println("Employee ID: " + staff.getEmployeeID());
        System.out.println("Name: " + staff.getEmployeeName());
        System.out.println("Outlet: (" + outletName + ")");
        System.out.println("\nClock in Succeful!");
        System.out.println("Date: " + today.format(dateFmt));
        System.out.println("Time: " + timeFormat(clockInTime));
    }

    public void clockOut(Employee staff, DataManager dm){
        if(!isClockedIn){
            System.out.println("You have not clocked in!");
            return;
        }

        String outletCode = staff.getOutlet();
        String outletName = dm.getOutletName(outletCode);
        isClockedIn = false;

        //dis is 2 find the duration between clock in and out
        clockOutTime = LocalDateTime.now();
        double hoursWorked = Duration.between(clockInTime, clockOutTime).toMinutes()/60.0;
        isClockedIn = false;
        clockInTime = null;

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("==== Attendance Clock Out =====");
        System.out.println("Employee ID: " + staff.getEmployeeID());
        System.out.println("Name: " + staff.getEmployeeName());
        System.out.println("Outlet : " + outletName);
        System.out.println("\nClock out succeful!");
        System.out.println("Date: " + today.format(dateFmt));
        System.out.println("Time: " + timeFormat(clockOutTime));
        System.out.printf("Total Hours Worked: %.1f hours\n ", hoursWorked);
    }

    // This is for testing, to simulate time passed
    public void simulateClockInHoursAgo(double hoursAgo) {
    this.clockInTime = LocalDateTime.now().minusMinutes((long)(hoursAgo * 60));
    this.isClockedIn = true;
    System.out.println("Simulated clock-in " + hoursAgo + " hours ago.");
    }
}