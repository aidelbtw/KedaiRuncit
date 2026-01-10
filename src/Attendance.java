import java.time.*;
import java.time.format.DateTimeFormatter;
public class Attendance {
    private LocalDateTime clockInTime;

    public boolean hasClockedIn() { return clockInTime != null; }

    public void clockIn(Employee staff) {
        if(hasClockedIn()) { System.out.println("Already in."); return; }
        clockInTime = LocalDateTime.now();
        System.out.println("Clocked IN at " + clockInTime.format(DateTimeFormatter.ofPattern("hh:mm a")));
    }

    public void clockOut(Employee staff) {
        if(!hasClockedIn()) { System.out.println("Not clocked in."); return; }
        LocalDateTime out = LocalDateTime.now();
        long minutes = Duration.between(clockInTime, out).toMinutes();
        System.out.println("Clocked OUT at " + out.format(DateTimeFormatter.ofPattern("hh:mm a")));
        System.out.println("Worked: " + minutes + " minutes.");
        clockInTime = null; // Reset
    }
}
