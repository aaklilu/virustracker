package et.bots.thevirustrackerbot.event.v1;

import et.bots.thevirustrackerbot.StatsDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsUpdateFailedEvent {

    private String subscriberId;
    private StatsDTO statsDTO;
}
