
package et.bots.thevirustrackerbot.stats.integration.tvt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Sitedatum {

    @JsonProperty("info")
    private Info info;

}
