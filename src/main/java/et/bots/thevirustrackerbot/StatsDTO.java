package et.bots.thevirustrackerbot;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StatsDTO {
    private String countryCode;
    private String province;
    private String latitude;
    private String longitude;
    private Date updatedOn;
    private Integer totalCases;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private Integer newCases;
    private Integer newDeaths;
    private Integer newRecoveries;
}
