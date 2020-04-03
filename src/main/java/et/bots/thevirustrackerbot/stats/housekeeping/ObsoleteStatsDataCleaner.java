package et.bots.thevirustrackerbot.stats.housekeeping;

import et.bots.thevirustrackerbot.stats.DailyStats;
import et.bots.thevirustrackerbot.stats.DailyStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class ObsoleteStatsDataCleaner {

    private final DailyStatsService dailyStatsService;

    @Value("${bot.stats.house-keeping.max-rows}")
    private Integer maxRows;

    @Async
    @Scheduled(fixedRateString = "${bot.stats.house-keeping.frequency.ms}")
    public void cleanupStatsData(){

        log.debug("HOUSEKEEPING - keeping stats rows to {}", maxRows);
        if(dailyStatsService.count() > maxRows){

            Page<DailyStats> latest =  dailyStatsService.findAll(PageRequest.of(0, maxRows, Sort.by("date").descending()));
            dailyStatsService.deleteAllExcept(latest.stream().map(dailyStats -> dailyStats.getId().toString()).collect(Collectors.toList()));
        }
    }
}
