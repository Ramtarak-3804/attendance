package attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class AttendanceReport {
    private static final Duration STANDARD_WORK_DAY = Duration.ofHours(8);

    private final User user;
    private final LocalDate from;
    private final LocalDate to;
    private final Map<AttendanceStatus, Long> counts;
    private final Duration totalWorked;
    private final Duration totalOvertime;
    private final long totalRecordedDays;
    private final long overtimeDays;

    public AttendanceReport(
            User user,
            LocalDate from,
            LocalDate to,
            Map<AttendanceStatus, Long> counts,
            Duration totalWorked,
            Duration totalOvertime,
            long totalRecordedDays,
            long overtimeDays) {
        this.user = Objects.requireNonNull(user, "user");
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        this.counts = Collections.unmodifiableMap(new EnumMap<>(counts));
        this.totalWorked = Objects.requireNonNull(totalWorked, "totalWorked");
        this.totalOvertime = Objects.requireNonNull(totalOvertime, "totalOvertime");
        this.totalRecordedDays = totalRecordedDays;
        this.overtimeDays = overtimeDays;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public Map<AttendanceStatus, Long> getCounts() {
        return counts;
    }

    public Duration getTotalWorked() {
        return totalWorked;
    }

    public Duration getTotalOvertime() {
        return totalOvertime;
    }

    public long getTotalRecordedDays() {
        return totalRecordedDays;
    }

    public long getOvertimeDays() {
        return overtimeDays;
    }

    public long getCount(AttendanceStatus status) {
        return counts.getOrDefault(status, 0L);
    }

    public static Duration getStandardWorkDay() {
        return STANDARD_WORK_DAY;
    }

    @Override
    public String toString() {
        return "AttendanceReport{" +
                "user=" + user +
                ", from=" + from +
                ", to=" + to +
                ", counts=" + counts +
                ", totalWorked=" + totalWorked +
                ", totalOvertime=" + totalOvertime +
                ", totalRecordedDays=" + totalRecordedDays +
                ", overtimeDays=" + overtimeDays +
                '}';
    }
}
