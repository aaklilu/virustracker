package et.bots.thevirustrackerbot.stats.integration.tvt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TvtApiConfig {

    @Bean
    public TvtGetCountryStatsRequestBuilder tvtGetCountryStatsRequestBuilder(){
        return new TvtGetCountryStatsRequestBuilder();
    }

    @Bean
    public TvtGetCountryStatsResponseTranslator tvtGetCountryStatsResponseTranslator(){
        return new TvtGetCountryStatsResponseTranslator();
    }
}
