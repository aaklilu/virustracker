package et.bots.thevirustrackerbot.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
public class StatusUpdater {
    private final DailyStatsService dailyStatsService;

    @Async
    @Scheduled(fixedRateString = "${status.update.frequency.ms}")
    public void updateStatus() throws InterruptedException {

        dailyStatsService.updateStats(null, null);
    }
}
