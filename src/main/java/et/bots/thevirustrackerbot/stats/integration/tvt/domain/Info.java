
package et.bots.thevirustrackerbot.stats.integration.tvt.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Info {

    @JsonProperty("ourid")
    private Integer ourid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("code")
    private String code;
    @JsonProperty("source")
    private String source;

}
