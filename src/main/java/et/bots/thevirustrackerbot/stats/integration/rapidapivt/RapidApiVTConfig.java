package et.bots.thevirustrackerbot.stats.integration.rapidapivt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RapidApiVTConfig {

    @Bean
    public RapidApiVTGetCountryStatsRequestBuilder rapidApiVTGetCountryStatsRequestBuilder(){
        return new RapidApiVTGetCountryStatsRequestBuilder();
    }

    @Bean
    public RapidApiVTGetCountryStatsResponseTranslator rapidApiVTGetCountryStatsResponseTranslator(){
        return new RapidApiVTGetCountryStatsResponseTranslator();
    }
}
