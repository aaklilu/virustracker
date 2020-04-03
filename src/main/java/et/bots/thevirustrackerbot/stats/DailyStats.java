package et.bots.thevirustrackerbot.stats;


import et.bots.thevirustrackerbot.SourceType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@SuperBuilder
@Table(indexes = {
        @Index(name = "daily_stats_idx_country_code_province__date", columnList = "countryCode, province, date")
})
@NoArgsConstructor
public class DailyStats {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    protected UUID id;

    @Version
    @Column(columnDefinition = "int Default 1")
    @Builder.Default
    private int version=1;

    private String countryCode;
    private String province;
    private String latitude;
    private String longitude;

    private LocalDateTime date;
    private Integer totalCases;
    private Integer totalActiveCases;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private Integer rank;

    private Integer newCases;
    private Integer newDeaths;
    private Integer newRecoveries;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) Default 'API'")
    @Builder.Default
    private SourceType sourceType = SourceType.API;
    private String sourceName;
}
