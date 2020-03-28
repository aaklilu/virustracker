
package et.bots.thevirustrackerbot.stats.integration.rapidapivt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class Data {

    @JsonProperty("lastChecked")
    private String lastChecked;
    @JsonProperty("covid19Stats")
    private List<Covid19Stat> covid19Stats = null;

}
