package et.bots.thevirustrackerbot.brain;

import et.bots.thevirustrackerbot.subscriber.Subscriber;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stimuli {
    private String message;
    private Subscriber subscriber;
}
