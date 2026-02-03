package attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    void save(AttendanceRecord record);

    AttendanceRecord findByUserAndDate(String userId, LocalDate date);

    List<AttendanceRecord> findByUserBetween(String userId, LocalDate from, LocalDate to);

    List<AttendanceRecord> findByDate(LocalDate date);
}
