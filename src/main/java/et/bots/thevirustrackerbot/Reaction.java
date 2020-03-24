package et.bots.thevirustrackerbot;

import et.bots.thevirustrackerbot.subscriber.Subscriber;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Reaction {
    private final String text;
    private Subscriber subscriber;
    private final List<List<Option>> optionVector;

    @Data
    @Builder
    public static class Option {

        private String text;
        private String callbackData;
        private boolean inline;
        private boolean requestLocation;
        private boolean requestContact;
        private boolean oneTime;
    }
}
