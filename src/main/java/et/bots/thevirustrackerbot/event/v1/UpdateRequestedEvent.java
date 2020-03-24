package et.bots.thevirustrackerbot.event.v1;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRequestedEvent {
    private String to;
    private String countryCode;
}
