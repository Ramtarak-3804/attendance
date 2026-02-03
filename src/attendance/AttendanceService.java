package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendancePolicy policy;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            AttendancePolicy policy
    ) {
        this.attendanceRepository = Objects.requireNonNull(attendanceRepository, "attendanceRepository");
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    public AttendanceRecord checkIn(String userId, LocalDateTime checkIn, String note) {
        requireUser(userId);
        Objects.requireNonNull(checkIn, "checkIn");
        LocalDate date = checkIn.toLocalDate();
        AttendanceRecord existing = attendanceRepository.findByUserAndDate(userId, date);
        if (existing != null) {
            if (existing.getStatus() == AttendanceStatus.ABSENT
                    || existing.getStatus() == AttendanceStatus.ON_LEAVE) {
                throw new AttendanceException("Cannot check in when status is " + existing.getStatus());
            }
            if (existing.hasCheckIn()) {
                throw new AttendanceException("Check-in already recorded for " + date);
            }
            AttendanceStatus status = policy.statusForCheckIn(checkIn);
            AttendanceRecord updated = existing
                    .withStatus(status)
                    .withCheckIn(checkIn)
                    .withNote(note);
            attendanceRepository.save(updated);
            return updated;
        }

        AttendanceStatus status = policy.statusForCheckIn(checkIn);
        AttendanceRecord record = AttendanceRecord.create(userId, date, status, checkIn, null, note);
        attendanceRepository.save(record);
        return record;
    }

    public AttendanceRecord checkOut(String userId, LocalDateTime checkOut) {
        requireUser(userId);
        Objects.requireNonNull(checkOut, "checkOut");
        LocalDate date = checkOut.toLocalDate();
        AttendanceRecord record = attendanceRepository.findByUserAndDate(userId, date);
        if (record == null) {
            throw new AttendanceException("No attendance record for " + date);
        }
        if (!record.hasCheckIn()) {
            throw new AttendanceException("Cannot check out without a check-in for " + date);
        }
        if (record.hasCheckOut()) {
            throw new AttendanceException("Check-out already recorded for " + date);
        }
        if (checkOut.isBefore(record.getCheckIn())) {
            throw new AttendanceException("Check-out cannot be before check-in");
        }
        AttendanceRecord updated = record.withCheckOut(checkOut);
        attendanceRepository.save(updated);
        return updated;
    }

    public AttendanceRecord markStatus(String userId, LocalDate date, AttendanceStatus status, String note) {
        requireUser(userId);
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(status, "status");
        AttendanceRecord existing = attendanceRepository.findByUserAndDate(userId, date);
        if (existing != null && (existing.hasCheckIn() || existing.hasCheckOut())) {
            throw new AttendanceException("Cannot overwrite a record with check-in/check-out");
        }
        AttendanceRecord record = AttendanceRecord.create(userId, date, status, null, null, note);
        attendanceRepository.save(record);
        return record;
    }

    public AttendanceRecord getAttendanceForDate(String userId, LocalDate date) {
        requireUser(userId);
        Objects.requireNonNull(date, "date");
        return attendanceRepository.findByUserAndDate(userId, date);
    }

    public List<AttendanceRecord> getAttendance(String userId, LocalDate from, LocalDate to) {
        requireUser(userId);
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) {
            throw new AttendanceException("End date cannot be before start date");
        }
        return attendanceRepository.findByUserBetween(userId, from, to);
    }

    private void requireUser(String userId) {
        Objects.requireNonNull(userId, "userId");
        if (userRepository.findById(userId) == null) {
            throw new AttendanceException("Unknown user: " + userId);
        }
    }
}
