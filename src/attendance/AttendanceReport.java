package attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class AttendanceReport {
    private final User user;
    private final LocalDate from;
    private final LocalDate to;
    private final Map<AttendanceStatus, Long> counts;
    private final Duration totalWorked;
    private final long totalRecordedDays;

    public AttendanceReport(
            User user,
            LocalDate from,
            LocalDate to,
            Map<AttendanceStatus, Long> counts,
            Duration totalWorked,
            long totalRecordedDays
    ) {
        this.user = Objects.requireNonNull(user, "user");
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        this.counts = Collections.unmodifiableMap(new EnumMap<>(counts));
        this.totalWorked = Objects.requireNonNull(totalWorked, "totalWorked");
        this.totalRecordedDays = totalRecordedDays;
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

    public long getTotalRecordedDays() {
        return totalRecordedDays;
    }

    public long getCount(AttendanceStatus status) {
        return counts.getOrDefault(status, 0L);
    }

    @Override
    public String toString() {
        return "AttendanceReport{" +
                "user=" + user +
                ", from=" + from +
                ", to=" + to +
                ", counts=" + counts +
                ", totalWorked=" + totalWorked +
                ", totalRecordedDays=" + totalRecordedDays +
                '}';
    }
}
