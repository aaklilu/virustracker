package et.bots.thevirustrackerbot.stats;

import et.bots.thevirustrackerbot.event.EventConfig;
import et.bots.thevirustrackerbot.stats.event.StatsEventHandlers;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.paga.thevirustrackerbot.stats")
@Import(EventConfig.class)
public class StatsConfig {

    public static final String STATS_PRODUCER_TEMPLATE_BEAN_NAME="statsProducerTemplate";

    @Qualifier(EventConfig.EVENT_PRODUCER_TEMPLATE_BEAN_NAME)
    private final ProducerTemplate eventProducerTemplate;
    private final CamelContext camelContext;
    private final DailyStatsRepository dailyStatsRepository;

    @Bean(STATS_PRODUCER_TEMPLATE_BEAN_NAME)
    public ProducerTemplate statsProducerTemplate(){

        ProducerTemplate producerTemplate= new DefaultProducerTemplate(camelContext);
        producerTemplate.start();

        return producerTemplate;
    }

    @Bean
    public DailyStatsService dailyStatsService(){
        return new DailyStatsServiceImpl(dailyStatsRepository, statsProducerTemplate(), eventProducerTemplate);
    }

    @Bean
    public StatusUpdater statusUpdater(){
        return new StatusUpdater(dailyStatsService());
    }

    @Bean
    public StatsEventHandlers statsEventHandlers(){
        return new StatsEventHandlers(dailyStatsService());
    }
}
