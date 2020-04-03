
package et.bots.thevirustrackerbot.stats.integration.tvt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Countrydatum {

    public Countrydatum(String val){
    }

    @JsonProperty("ourid")
    private Integer ourid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("code")
    private String code;
    @JsonProperty("source")
    private String source;
    @JsonProperty("info")
    private Info info;
    @JsonProperty("total_cases")
    private Integer totalCases;
    @JsonProperty("total_recovered")
    private Integer totalRecovered;
    @JsonProperty("total_unresolved")
    private Integer totalUnresolved;
    @JsonProperty("total_deaths")
    private Integer totalDeaths;
    @JsonProperty("total_new_cases_today")
    private Integer totalNewCasesToday;
    @JsonProperty("total_new_deaths_today")
    private Integer totalNewDeathsToday;
    @JsonProperty("total_active_cases")
    private Integer totalActiveCases;
    @JsonProperty("total_serious_cases")
    private Integer totalSeriousCases;
    @JsonProperty("total_danger_rank")
    private Integer totalDangerRank;

}
