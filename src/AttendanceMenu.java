import java.util.Scanner;
public class AttendanceMenu {
 public static void menu(Employee staff, DataManager dm, Attendance attendance){        
    Scanner input = new Scanner(System.in);
        boolean goBack = false;
        
        while (!goBack){
            System.out.println("\n===== Attendance Menu =====");

            if(!attendance.hasClockedIn()){
                System.out.println("1. Clock In");
                System.out.println("2. Back");
            } else {
                System.out.println("1. Clock Out");
                System.out.println("2. Back");
            }

            System.out.println("Choose: ");
            int choice = input.nextInt();
            input.nextLine();
            if(!attendance.hasClockedIn()){
                switch (choice) {
                    case 1:
                        attendance.clockIn(staff, dm);
                        goBack = true;
                        break;
                    case 2:
                        goBack = true;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } else {
                switch (choice) {
                    case 1:
                        // This is if u want to test whether the hour counter is working
                        // attendance.simulateClockInHoursAgo(5.5);
                        attendance.clockOut(staff, dm);
                        goBack = true;
                        break;
                    case 2:
                        goBack = true;
                        break;
                    default:
                        System.out.println("Invalid Option");
                        break;
                }
            }
        }
    }
}
