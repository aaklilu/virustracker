package et.bots.thevirustrackerbot.stats.integration.herokuvt;

import et.bots.thevirustrackerbot.stats.integration.GetCountryStatsRequestBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class HerokuVTConfig {

    @Bean
    public HerokuVTGetCountryStatsRequestBuilder herokuVTGetCountryStatsRequestBuilder(){
        return new HerokuVTGetCountryStatsRequestBuilder();
    }

    @Bean
    public HerokuVTGetCountryStatsResponseTranslator herokuVTGetCountryStatsResponseTranslator(){
        return new HerokuVTGetCountryStatsResponseTranslator();
    }
}
