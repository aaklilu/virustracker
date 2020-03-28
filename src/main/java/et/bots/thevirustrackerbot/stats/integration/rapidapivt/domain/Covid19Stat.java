
package et.bots.thevirustrackerbot.stats.integration.rapidapivt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Covid19Stat {

    @JsonProperty("city")
    private String city;
    @JsonProperty("province")
    private String province;
    @JsonProperty("country")
    private String country;
    @JsonProperty("lastUpdate")
    private String lastUpdate;
    @JsonProperty("keyId")
    private String keyId;
    @JsonProperty("confirmed")
    private Integer confirmed;
    @JsonProperty("deaths")
    private Integer deaths;
    @JsonProperty("recovered")
    private Integer recovered;

}
