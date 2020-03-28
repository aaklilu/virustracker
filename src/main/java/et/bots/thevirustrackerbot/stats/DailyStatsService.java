package et.bots.thevirustrackerbot.stats;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyStatsService {
    Iterable<DailyStats> findAll();
    Optional<DailyStats> findLatestStats(String countryCode);
    Optional<DailyStats> findByCountryCodeProvinceAndDateRange(String countryCode, String province, LocalDateTime from, LocalDateTime to);
    DailyStats create(DailyStats dailyStats);
    DailyStats update(DailyStats dailyStats);
    void delete(DailyStats dailyStats);
    void updateStats(String countryCode, String subscriberId);
}
