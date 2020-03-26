package et.bots.thevirustrackerbot.stats.integration.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryStatsProviderRepository implements StatsProviderRepository, Iterable<StatsProvider> {

    private final Map<String, StatsProvider> statsProviders;

    /**
     * Constructs an {@code InMemoryStatsProviderRepository} using the provided parameters.
     *
     * @param statsProviders the integration service registration(s)
     */
    public InMemoryStatsProviderRepository(StatsProvider... statsProviders) {
        this(Arrays.asList(statsProviders));
    }

    /**
     * Constructs an {@code InMemoryStatsProviderRepository} using the provided parameters.
     *
     * @param statsProviders the services(s)
     */
    public InMemoryStatsProviderRepository(List<StatsProvider> statsProviders) {
        this(createIntegrationServicesMap(statsProviders));
    }

    private static Map<String, StatsProvider> createIntegrationServicesMap(List<StatsProvider> statsProviders) {
        Assert.notEmpty(statsProviders, "integrationServices cannot be empty");
        return toUnmodifiableConcurrentMap(statsProviders);
    }

    private static Map<String, StatsProvider> toUnmodifiableConcurrentMap(List<StatsProvider> statsProviders) {
        ConcurrentHashMap<String, StatsProvider> result = new ConcurrentHashMap<>();

        for (StatsProvider statsProvider : statsProviders) {

            if (result.containsKey(statsProvider.getId())) {
                throw new IllegalStateException(String.format("Duplicate key %s", statsProvider.getId()));
            }
            result.put(statsProvider.getId(), statsProvider);

        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Constructs an {@code InMemoryStatsProviderRepository} using the provided values
     *
     * @param statsProviders the {@code Map} of provider(s)
     */
    public InMemoryStatsProviderRepository(Map<String, StatsProvider> statsProviders) {
        Assert.notNull(statsProviders, "statsProviders cannot be null");
        this.statsProviders = statsProviders;
    }

    @Override
    public StatsProvider findById(String id) {
        Assert.notNull(id, "id cannot be empty");

        return this.statsProviders.get(id);
    }

    /**
     * Returns an {@code Iterator} of {@link StatsProvider}.
     *
     * @return an {@code Iterator<StatsProvider>}
     */
    @Override
    public Iterator<StatsProvider> iterator() {
        return this.statsProviders.values().iterator();
    }
}
