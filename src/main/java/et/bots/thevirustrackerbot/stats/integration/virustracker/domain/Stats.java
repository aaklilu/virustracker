
package et.bots.thevirustrackerbot.stats.integration.virustracker.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Stats {

    @JsonProperty("latest")
    private Latest latest;
    @JsonProperty("locations")
    private List<Location> locations = null;

}
