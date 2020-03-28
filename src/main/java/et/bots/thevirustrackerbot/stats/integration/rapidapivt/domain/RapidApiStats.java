
package et.bots.thevirustrackerbot.stats.integration.rapidapivt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class RapidApiStats {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Data data;

}
