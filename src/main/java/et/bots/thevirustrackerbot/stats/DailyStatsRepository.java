package et.bots.thevirustrackerbot.stats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyStatsRepository extends PagingAndSortingRepository<DailyStats, UUID> {

    Optional<DailyStats> findByCountryCodeAndProvinceAndDateBetween(String countryCode, String province, LocalDateTime from, LocalDateTime to);
    Optional<DailyStats> findFirstByCountryCodeOrderByDateDesc(String countryCode);

    @Modifying
    @Transactional
    @Query("delete from DailyStats d where d.id not in (:ids)")
    void deleteAllExcept(@Param("ids") List<String> ids);
}
