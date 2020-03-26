package et.bots.thevirustrackerbot.stats.integration.provider;

public interface StatsProviderRepository {
    StatsProvider findById(String id);
}
