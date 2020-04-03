package et.bots.thevirustrackerbot.stats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyStatsService {
    Page<DailyStats> findAll(Pageable pageable);
    Optional<DailyStats> findLatestStats(String countryCode);
    Optional<DailyStats> findByCountryCodeProvinceAndDateRange(String countryCode, String province, LocalDateTime from, LocalDateTime to);
    DailyStats create(DailyStats dailyStats);
    DailyStats update(DailyStats dailyStats);
    void delete(DailyStats dailyStats);
    void updateStats(String countryCode, String subscriberId);
    long count();
    void deleteAllExcept(List<String> ids);
}
