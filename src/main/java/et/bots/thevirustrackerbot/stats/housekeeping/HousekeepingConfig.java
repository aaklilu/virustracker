package et.bots.thevirustrackerbot.stats.housekeeping;

import et.bots.thevirustrackerbot.stats.DailyStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class HousekeepingConfig {

    private final DailyStatsService dailyStatsService;

    @Bean
    @Conditional(StatsRowsLimitedCondition.class)
    public ObsoleteStatsDataCleaner obsoleteStatsDataCleaner(){
        return new ObsoleteStatsDataCleaner(dailyStatsService);
    }
}
