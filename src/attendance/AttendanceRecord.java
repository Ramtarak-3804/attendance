package attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public final class AttendanceRecord {
    private final String userId;
    private final LocalDate date;
    private final AttendanceStatus status;
    private final LocalDateTime checkIn;
    private final LocalDateTime checkOut;
    private final String note;

    private AttendanceRecord(
            String userId,
            LocalDate date,
            AttendanceStatus status,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String note
    ) {
        this.userId = requireNonBlank(userId, "userId");
        this.date = Objects.requireNonNull(date, "date");
        this.status = Objects.requireNonNull(status, "status");
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.note = note;
    }

    public static AttendanceRecord create(
            String userId,
            LocalDate date,
            AttendanceStatus status,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String note
    ) {
        return new AttendanceRecord(userId, date, status, checkIn, checkOut, note);
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public String getNote() {
        return note;
    }

    public AttendanceRecord withStatus(AttendanceStatus newStatus) {
        return new AttendanceRecord(userId, date, newStatus, checkIn, checkOut, note);
    }

    public AttendanceRecord withCheckIn(LocalDateTime newCheckIn) {
        return new AttendanceRecord(userId, date, status, newCheckIn, checkOut, note);
    }

    public AttendanceRecord withCheckOut(LocalDateTime newCheckOut) {
        return new AttendanceRecord(userId, date, status, checkIn, newCheckOut, note);
    }

    public AttendanceRecord withNote(String newNote) {
        return new AttendanceRecord(userId, date, status, checkIn, checkOut, newNote);
    }

    public boolean hasCheckIn() {
        return checkIn != null;
    }

    public boolean hasCheckOut() {
        return checkOut != null;
    }

    public Duration workedDuration() {
        if (checkIn == null || checkOut == null) {
            return Duration.ZERO;
        }
        return Duration.between(checkIn, checkOut);
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "userId='" + userId + '\'' +
                ", date=" + date +
                ", status=" + status +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", note='" + note + '\'' +
                '}';
    }
}
