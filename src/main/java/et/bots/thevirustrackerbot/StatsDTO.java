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
    @Builder.Default
    private Integer totalActiveCases=0;
    @Builder.Default
    private Integer rank=0;
    private Integer totalDeaths;
    private Integer totalRecovered;
    @Builder.Default
    private Integer newCases=0;
    @Builder.Default
    private Integer newDeaths=0;
    @Builder.Default
    private Integer newRecoveries=0;

    @Builder.Default
    private SourceType sourceType=SourceType.API;

    private String sourceName;
}
