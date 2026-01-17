import java.util.Scanner;

public class Main_ChangeOnlyYourPart {
    public static void main(String[] args) {
        DataManager dm = new DataManager();
        
        // PATHS: Go UP one level from 'src' to find 'data'
        dm.loadEmployees("../data/employee.csv");
        dm.loadOutlets("../data/outlet.csv");
        dm.loadProducts("../data/model.csv");

        Scanner input = new Scanner(System.in);
        
        // Debug check to help you avoid "File Not Found" errors
        if (dm.getEmployees().size() == 0) {
            System.out.println("ERROR: No data loaded.");
            System.out.println("Please ensure your folder structure is correct:");
            System.out.println("KedaiRuncit/data/   (contains CSV files)");
            System.out.println("KedaiRuncit/src/    (contains Java files)");
            return;
        }

        while(true) {
            System.out.println("\n=== SYSTEM MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Shutdown (Auto-Email Report)");
            System.out.print("Choice: ");
            
            int ch = -1;
            try { ch = input.nextInt(); } catch(Exception e) { input.nextLine(); }
            
            if(ch == 2) {
                System.out.println("Initiating Shutdown Sequence...");
                
                // === FIX START: Ask for email so we can pass it to the service ===
                input.nextLine(); // Clear the buffer after nextInt()
                System.out.print("Enter Recipient Email: ");
                String email = input.nextLine();
                
                // Now passing BOTH DataManager and the email string
                EmailService.sendDailyReport(dm, email); 
                // === FIX END ===
                
                System.out.println("System Closed. Goodbye.");
                break;
            }
            
            if(ch == 1) {
                Employee user = LoginSystem.login(dm.getEmployees());
                if(user != null) {
                    // user.getAttendance().setDataManager(dm); // Ensure Attendance has this method if you use it
                    boolean loggedIn = true;
                    while(loggedIn) {
                        System.out.println("\n--- Dashboard: " + user.getEmployeeName() + " ---");
                        System.out.println("1. Attendance");
                        System.out.println("2. Stock Management");
                        System.out.println("3. Sales System");
                        System.out.println("4. Search Products");
                        System.out.println("5. Edit System");
                        System.out.println("6. Analytics & Performance");
                        System.out.println("7. Register New Employee");
                        System.out.println("8. Logout");
                        System.out.print("Choice: ");
                        
                        int action = -1; 
                        try { action = input.nextInt(); } catch(Exception e) { input.nextLine(); }

                        switch(action) {
                        case 1: 
                            // Pass 'dm' into clockIn and clockOut
                            if(!user.getAttendance().hasClockedIn()) user.getAttendance().clockIn(user, dm);
                            else user.getAttendance().clockOut(user, dm);
                            break;
                            case 2: StockManagement.manage(dm, user); break;
                            case 3: SalesSystem.sell(dm, user); break;
                            case 4: SearchSystem.search(dm); break;
                            case 5: EditSystem.edit(dm, user); break;
                            case 6: AnalyticsSystem.showMenu(); break;
                            case 7: 
                                if(user.getRole().equalsIgnoreCase("Manager")) LoginSystem.registerEmployee(dm); 
                                else System.out.println("Managers only.");
                                break;
                            case 8: loggedIn = false; break;
                        }
                    }
                }
            }
        }
    }
}
