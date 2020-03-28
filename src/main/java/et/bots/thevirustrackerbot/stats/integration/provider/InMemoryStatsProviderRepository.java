package et.bots.thevirustrackerbot.stats.integration.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

            if (result.containsKey(statsProvider.getName())) {
                throw new IllegalStateException(String.format("Duplicate key %s", statsProvider.getName()));
            }
            result.put(statsProvider.getName(), statsProvider);

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
        Assert.notNull(id, "name cannot be empty");

        return this.statsProviders.get(id);
    }

    @Override
    public Set<StatsProvider> findAllByCountryOrGlobal(String countryCode) {
        Set<StatsProvider> providers = StringUtils.isEmpty(countryCode)? new LinkedHashSet<>(): statsProviders
                .values()
                .stream()
                .filter(value -> countryCode.equalsIgnoreCase(value.getCountryCode()))
                .sorted(Comparator.comparingInt(StatsProvider::getFailoverOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        providers.addAll(statsProviders
                .values()
                .stream()
                .filter(value -> StringUtils.isEmpty(value.getCountryCode()))
                .sorted(Comparator.comparingInt(StatsProvider::getFailoverOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        return providers;
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
