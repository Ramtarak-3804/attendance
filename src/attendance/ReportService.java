package attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public ReportService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = Objects.requireNonNull(attendanceRepository, "attendanceRepository");
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
    }

    public AttendanceReport generateUserReport(String userId, LocalDate from, LocalDate to) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) {
            throw new AttendanceException("End date cannot be before start date");
        }
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AttendanceException("Unknown user: " + userId);
        }

        List<AttendanceRecord> records = attendanceRepository.findByUserBetween(userId, from, to);
        Map<AttendanceStatus, Long> counts = new EnumMap<>(AttendanceStatus.class);
        Duration totalWorked = Duration.ZERO;
        for (AttendanceRecord record : records) {
            counts.put(record.getStatus(), counts.getOrDefault(record.getStatus(), 0L) + 1);
            totalWorked = totalWorked.plus(record.workedDuration());
        }
        return new AttendanceReport(user, from, to, counts, totalWorked, records.size());
    }
}
