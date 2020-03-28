package et.bots.thevirustrackerbot.stats.integration.config;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Condition that matches if any {@code bot.stats.provider}
 * properties are defined.
 *
 * */
public class IntegrationServicesConfiguredCondition extends SpringBootCondition {

    private static final Bindable<Map<String, IntegrationProperties.StatsProvider>> PROVIDERS_MAP = Bindable
            .mapOf(String.class, IntegrationProperties.StatsProvider.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("HerokuStats Providers Configured Condition");
        Map<String, IntegrationProperties.StatsProvider> registrations = getProviders(context.getEnvironment());
        if (!registrations.isEmpty()) {
            return ConditionOutcome.match(message.foundExactly("registered stats providers " + registrations.values().stream()
                    .map(IntegrationProperties.StatsProvider::getName).collect(Collectors.joining(", "))));
        }
        return ConditionOutcome.noMatch(message.notAvailable("registered stats providers"));
    }

    private Map<String, IntegrationProperties.StatsProvider> getProviders(Environment environment) {
        return Binder.get(environment).bind("bot.stats.provider", PROVIDERS_MAP)
                .orElse(Collections.emptyMap());
    }

}
