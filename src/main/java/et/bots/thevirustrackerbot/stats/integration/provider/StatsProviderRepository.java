package et.bots.thevirustrackerbot.stats.integration.provider;

import java.util.List;
import java.util.Set;

public interface StatsProviderRepository {
    StatsProvider findById(String id);
    Set<StatsProvider> findAllByCountryOrGlobal(String countryCode);
}
