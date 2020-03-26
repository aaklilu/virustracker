
package et.bots.thevirustrackerbot.stats.integration.virustracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Latest {

    @JsonProperty("confirmed")
    private Integer confirmed;
    @JsonProperty("deaths")
    private Integer deaths;
    @JsonProperty("recovered")
    private Integer recovered;

}
