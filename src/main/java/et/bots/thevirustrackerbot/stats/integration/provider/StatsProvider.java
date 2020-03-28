package et.bots.thevirustrackerbot.stats.integration.provider;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class StatsProvider {

    private int failoverOrder;
    private String id;
    private String name;
    private String countryCode;

    @Builder.Default
    private Map<String, String> metadata = new HashMap<>();
}
