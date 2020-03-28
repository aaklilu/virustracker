
package et.bots.thevirustrackerbot.stats.integration.herokuvt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HerokuStats {

    @JsonProperty("latest")
    private Latest latest;
    @JsonProperty("locations")
    private List<Location> locations = null;

}
