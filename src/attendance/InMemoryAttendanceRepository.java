package attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAttendanceRepository implements AttendanceRepository {
    private final Map<String, Map<LocalDate, AttendanceRecord>> store = new ConcurrentHashMap<>();

    @Override
    public void save(AttendanceRecord record) {
        Objects.requireNonNull(record, "record");
        store.computeIfAbsent(record.getUserId(), ignored -> new ConcurrentHashMap<>())
                .put(record.getDate(), record);
    }

    @Override
    public AttendanceRecord findByUserAndDate(String userId, LocalDate date) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(date, "date");
        Map<LocalDate, AttendanceRecord> userRecords = store.get(userId);
        if (userRecords == null) {
            return null;
        }
        return userRecords.get(date);
    }

    @Override
    public List<AttendanceRecord> findByUserBetween(String userId, LocalDate from, LocalDate to) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Map<LocalDate, AttendanceRecord> userRecords = store.get(userId);
        if (userRecords == null) {
            return Collections.emptyList();
        }
        List<AttendanceRecord> result = new ArrayList<>();
        for (Map.Entry<LocalDate, AttendanceRecord> entry : userRecords.entrySet()) {
            LocalDate date = entry.getKey();
            if (!date.isBefore(from) && !date.isAfter(to)) {
                result.add(entry.getValue());
            }
        }
        result.sort(Comparator.comparing(AttendanceRecord::getDate));
        return result;
    }

    @Override
    public List<AttendanceRecord> findByDate(LocalDate date) {
        Objects.requireNonNull(date, "date");
        List<AttendanceRecord> result = new ArrayList<>();
        for (Map<LocalDate, AttendanceRecord> userRecords : store.values()) {
            AttendanceRecord record = userRecords.get(date);
            if (record != null) {
                result.add(record);
            }
        }
        result.sort(Comparator.comparing(AttendanceRecord::getUserId));
        return result;
    }
}
