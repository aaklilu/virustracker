
package et.bots.thevirustrackerbot.stats.integration.tvt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SiteData {

    @JsonProperty("sitedata")
    private List<Sitedatum> sitedata = null;
    @JsonProperty("countryitems")
    private List<Map<String,Countrydatum>> countryitems = null;

}
