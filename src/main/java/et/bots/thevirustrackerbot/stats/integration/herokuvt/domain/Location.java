
package et.bots.thevirustrackerbot.stats.integration.herokuvt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    @JsonProperty("coordinates")
    private Coordinates coordinates;
    @JsonProperty("country")
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("last_updated")
    private String lastUpdated;
    @JsonProperty("latest")
    private Latest latest;
    @JsonProperty("province")
    private String province;

}
