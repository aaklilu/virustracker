package et.bots.thevirustrackerbot.stats.event;

import et.bots.thevirustrackerbot.event.v1.UpdateRequestedEvent;
import et.bots.thevirustrackerbot.stats.DailyStatsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consume;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class StatsEventHandlers {

    private final DailyStatsService dailyStatsService;

    @Getter
    @Value("${bot.events.queue-uri.main}")
    private String eventEndpointUrl;

    @Consume(property = "eventEndpointUrl", predicate = "${body} is 'com.paga.thevirustrackerbot.event.v1.UpdateRequestedEvent'")
    public CompletableFuture<Void> handleUpdateRequestedEvent(UpdateRequestedEvent event){

        dailyStatsService.updateStats(event.getCountryCode(), event.getTo());
        return CompletableFuture.completedFuture(null);
    }
}
