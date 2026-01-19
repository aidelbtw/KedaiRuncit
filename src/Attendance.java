import java.time.*;
import java.time.format.DateTimeFormatter;

public class Attendance {

    private LocalDateTime clockInTime;

    public boolean hasClockedIn() {
        return clockInTime != null;
    }

    private String formatTime(LocalDateTime t) {
        return t.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public void clockIn(Employee staff, DataManager dm) {

        if (hasClockedIn()) {
            System.out.println("Already clocked in.");
            return;
        }

        clockInTime = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        String outletCode = staff.getOutlet();
        String outletName = (dm != null) ? dm.getOutletName(outletCode) : "HQ"; 

        System.out.println("\n=== Attendance Clock In ===");
        System.out.println("Employee ID: " + staff.getEmployeeID());
        System.out.println("Name: " + staff.getEmployeeName());
        System.out.println("Outlet: " + outletCode + " (" + outletName + ")\n");
        System.out.println("Clock In Successful!");
        System.out.println("Date: " + today);
        System.out.println("Time: " + formatTime(clockInTime));
    }

    public void clockOut(Employee staff, DataManager dm) {

        if (!hasClockedIn()) {
            System.out.println("Not clocked in.");
            return;
        }

        LocalDateTime clockOutTime = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        long minutes = Duration.between(clockInTime, clockOutTime).toMinutes();
        double hours = minutes / 60.0;

        String outletCode = staff.getOutlet();
        String outletName = (dm != null) ? dm.getOutletName(outletCode) : "HQ";

        System.out.println("\n=== Attendance Clock Out ===");
        System.out.println("Employee ID: " + staff.getEmployeeID());
        System.out.println("Name: " + staff.getEmployeeName());
        System.out.println("Outlet: " + outletCode + " (" + outletName + ")\n");
        System.out.println("Clock Out Successful!");
        System.out.println("Date: " + today);
        System.out.println("Time: " + formatTime(clockOutTime));

        //to show minutes if duration is short
        if (minutes == 0) {
            System.out.println("Total Duration: Less than 1 minute");
        } else if (minutes < 60) {
            System.out.println("Total Duration: " + minutes + " minutes");
        } else {
            System.out.printf("Total Hours Worked: %.1f hours%n", hours);
        }

        clockInTime = null;
    }
}