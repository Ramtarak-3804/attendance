package attendance;

import java.time.LocalDateTime;

public interface AttendancePolicy {
    AttendanceStatus statusForCheckIn(LocalDateTime checkIn);
}
