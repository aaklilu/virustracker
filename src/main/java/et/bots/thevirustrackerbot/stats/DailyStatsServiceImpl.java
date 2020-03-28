package et.bots.thevirustrackerbot.stats;

import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.event.v1.StatsUpdateFailedEvent;
import et.bots.thevirustrackerbot.event.v1.StatsUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DailyStatsServiceImpl implements DailyStatsService {

    private static final long TIMEOUT_VALUE=120;
    private static final TimeUnit TIMEOUT_UNIT=TimeUnit.SECONDS;

    private final DailyStatsRepository dailyStatsRepository;
    private final ProducerTemplate statsProducerTemplate;
    private final ProducerTemplate eventProducerTemplate;

    @Override
    public Iterable<DailyStats> findAll() {
        return dailyStatsRepository.findAll();
    }

    @Override
    public Optional<DailyStats> findLatestStats(String countryCode) {
        return dailyStatsRepository.findFirstByCountryCodeOrderByDateDesc(countryCode);
    }

    @Override
   public Optional<DailyStats> findByCountryCodeProvinceAndDateRange(String countryCode, String province, LocalDateTime from, LocalDateTime to) {
        return dailyStatsRepository.findByCountryCodeAndProvinceAndDateBetween(countryCode,province,from, to);
    }

    @Override
    public DailyStats create(DailyStats dailyStats) {
        return dailyStatsRepository.save(dailyStats);
    }

    @Override
    public DailyStats update(DailyStats dailyStats) {
        return dailyStatsRepository.save(dailyStats);
    }

    @Override
    public void delete(DailyStats dailyStats) {
        dailyStatsRepository.delete(dailyStats);
    }

    @Override
    public void updateStats(String countryCode, String subscriberId){
        try {
            List<StatsDTO> statsDTOs = statsProducerTemplate.asyncRequestBodyAndHeader(StatsConstants.ENDPOINT_GET_STATS,"", StatsConstants.HEADER_COUNTRY_CODE, countryCode, List.class).get(TIMEOUT_VALUE, TIMEOUT_UNIT);
            statsDTOs
                    .stream()
                    .filter(statsDTO -> !StringUtils.isEmpty(statsDTO.getCountryCode()))
                    .collect(Collectors.groupingBy(StatsDTO::getCountryCode, Collectors.collectingAndThen(Collectors.toList(), list -> StatsDTO
                            .builder()
                            .countryCode(list.get(0).getCountryCode())
                            .newCases(list.stream().mapToInt(StatsDTO::getNewCases).sum())
                            .newDeaths( list.stream().mapToInt(StatsDTO::getNewDeaths).sum())
                            .newRecoveries( list.stream().mapToInt(StatsDTO::getNewRecoveries).sum())
                            .totalCases( list.stream().mapToInt(StatsDTO::getTotalCases).sum())
                            .totalDeaths( list.stream().mapToInt(StatsDTO::getTotalDeaths).sum())
                            .totalRecovered( list.stream().mapToInt(StatsDTO::getTotalRecovered).sum())
                            .build())))
                    .values()
                    .forEach(statsDTO -> {

                        Optional<DailyStats> existing = this.findByCountryCodeProvinceAndDateRange(statsDTO.getCountryCode(), statsDTO.getProvince(), LocalDate.now().atStartOfDay(), LocalDateTime.now());

                        if(!existing.isPresent()) {
                            this.create(DailyStats
                                    .builder()
                                    .countryCode(statsDTO.getCountryCode())
                                    .province(statsDTO.getProvince())
                                    .latitude(statsDTO.getLatitude())
                                    .longitude(statsDTO.getLongitude())
                                    .date(LocalDateTime.now())
                                    .totalCases(statsDTO.getTotalCases())
                                    .totalDeaths(statsDTO.getTotalDeaths())
                                    .totalRecovered(statsDTO.getTotalRecovered())
                                    .newCases(0)
                                    .newDeaths(0)
                                    .newRecoveries(0)
                                    .build());

                            eventProducerTemplate.sendBody(StatsUpdatedEvent.builder().statsDTO(statsDTO).build());
                        }else if(statsDTO.getTotalCases() > existing.get().getTotalCases()
                                || statsDTO.getTotalDeaths() > existing.get().getTotalDeaths()
                                || statsDTO.getTotalRecovered() > existing.get().getTotalRecovered()){

                            DailyStats dailyStats = existing.get();
                            dailyStats.setNewCases(dailyStats.getNewCases()+ (statsDTO.getTotalCases() - dailyStats.getTotalCases()));
                            dailyStats.setNewDeaths(dailyStats.getNewDeaths()+ (statsDTO.getTotalDeaths() - dailyStats.getTotalDeaths()));
                            dailyStats.setNewRecoveries(dailyStats.getNewRecoveries()+ (statsDTO.getTotalRecovered() - dailyStats.getTotalRecovered()));

                            dailyStats.setTotalCases(statsDTO.getTotalCases());
                            dailyStats.setTotalDeaths(statsDTO.getTotalDeaths());
                            dailyStats.setTotalRecovered(statsDTO.getTotalRecovered());

                            this.update(dailyStats);

                            //update the dto as well...it doesn't contail.n vales for NEW cases as its calculated here
                            statsDTO.setNewCases(dailyStats.getNewCases());
                            statsDTO.setNewDeaths(dailyStats.getNewDeaths());
                            statsDTO.setNewRecoveries(dailyStats.getNewRecoveries());
                            eventProducerTemplate.sendBody(StatsUpdatedEvent.builder().statsDTO(statsDTO).build());
                        }else if(!StringUtils.isEmpty(subscriberId)){

                            DailyStats dailyStats = existing.get();

                            statsDTO.setNewCases(dailyStats.getNewCases());
                            statsDTO.setNewDeaths(dailyStats.getNewDeaths());
                            statsDTO.setNewRecoveries(dailyStats.getNewRecoveries());

                            statsDTO.setTotalCases(dailyStats.getTotalCases());
                            statsDTO.setTotalDeaths(dailyStats.getTotalDeaths());
                            statsDTO.setTotalRecovered(dailyStats.getTotalRecovered());

                            eventProducerTemplate.sendBody(StatsUpdatedEvent.builder().subscriberId(subscriberId).statsDTO(statsDTO).build());
                        }
                    });
        } catch (Exception e) {
            log.error("Unable to update status: {}" ,e.getMessage(), e);
            if(!StringUtils.isEmpty(subscriberId)){
                Optional<DailyStats> existing = this.findLatestStats(countryCode);

                if(existing.isPresent()){
                    DailyStats dailyStats = existing.get();
                    StatsDTO statsDTO = StatsDTO
                            .builder()
                            .newCases(dailyStats.getNewCases())
                            .newDeaths(dailyStats.getNewDeaths())
                            .newRecoveries(dailyStats.getNewRecoveries())
                            .totalCases(dailyStats.getTotalCases())
                            .totalDeaths(dailyStats.getTotalDeaths())
                            .totalRecovered(dailyStats.getTotalRecovered())
                            .build();

                    eventProducerTemplate.sendBody(StatsUpdateFailedEvent.builder().subscriberId(subscriberId).statsDTO(statsDTO).build());
                }else{
                    eventProducerTemplate.sendBody(StatsUpdateFailedEvent.builder().subscriberId(subscriberId).build());
                }
            }
        }
    }
}
