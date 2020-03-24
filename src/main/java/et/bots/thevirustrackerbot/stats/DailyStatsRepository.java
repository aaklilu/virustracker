package et.bots.thevirustrackerbot.stats;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface DailyStatsRepository extends CrudRepository<DailyStats, UUID> {

    Optional<DailyStats> findByCountryCodeAndProvinceAndDateBetween(String countryCode, String province, LocalDateTime from, LocalDateTime to);
}
