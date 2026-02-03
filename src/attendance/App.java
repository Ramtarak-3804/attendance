package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class App {
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        AttendanceRepository attendanceRepository = new InMemoryAttendanceRepository();
        AttendancePolicy policy = new DefaultAttendancePolicy(LocalTime.of(9, 15));

        AttendanceService attendanceService = new AttendanceService(
                attendanceRepository,
                userRepository,
                policy
        );
        ReportService reportService = new ReportService(attendanceRepository, userRepository);

        User alice = new User("U100", "Alice Patel", Role.EMPLOYEE);
        User bob = new User("U200", "Bob Singh", Role.EMPLOYEE);
        userRepository.add(alice);
        userRepository.add(bob);

        LocalDate today = LocalDate.now();
        attendanceService.checkIn("U100", LocalDateTime.of(today, LocalTime.of(8, 55)), "Morning check-in");
        attendanceService.checkOut("U100", LocalDateTime.of(today, LocalTime.of(17, 30)));

        attendanceService.checkIn("U200", LocalDateTime.of(today, LocalTime.of(9, 30)), "Traffic delay");
        attendanceService.checkOut("U200", LocalDateTime.of(today, LocalTime.of(18, 5)));

        LocalDate yesterday = today.minusDays(1);
        attendanceService.markStatus("U100", yesterday, AttendanceStatus.ON_LEAVE, "Medical appointment");
        attendanceService.markStatus("U200", yesterday, AttendanceStatus.ABSENT, "No call");

        AttendanceReport report = reportService.generateUserReport(
                "U100",
                yesterday,
                today
        );

        System.out.println("Report for " + report.getUser().getName());
        System.out.println("Range: " + report.getFrom() + " to " + report.getTo());
        System.out.println("Recorded days: " + report.getTotalRecordedDays());
        System.out.println("Present: " + report.getCount(AttendanceStatus.PRESENT));
        System.out.println("Late: " + report.getCount(AttendanceStatus.LATE));
        System.out.println("Absent: " + report.getCount(AttendanceStatus.ABSENT));
        System.out.println("On leave: " + report.getCount(AttendanceStatus.ON_LEAVE));
        System.out.println("Total worked: " + report.getTotalWorked());
    }
}
