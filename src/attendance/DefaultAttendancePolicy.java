package attendance;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class DefaultAttendancePolicy implements AttendancePolicy {
    private final LocalTime lateAfter;

    public DefaultAttendancePolicy(LocalTime lateAfter) {
        this.lateAfter = Objects.requireNonNull(lateAfter, "lateAfter");
    }

    public static DefaultAttendancePolicy standardOfficePolicy() {
        return new DefaultAttendancePolicy(LocalTime.of(9, 15));
    }

    @Override
    public AttendanceStatus statusForCheckIn(LocalDateTime checkIn) {
        Objects.requireNonNull(checkIn, "checkIn");
        return checkIn.toLocalTime().isAfter(lateAfter)
                ? AttendanceStatus.LATE
                : AttendanceStatus.PRESENT;
    }
}
