package et.bots.thevirustrackerbot.stats.housekeeping;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class StatsRowsLimitedCondition implements Condition {

    private static final String MAX_ROWS_PROPERTY="bot.stats.house-keeping.max-rows";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Integer maxRows = context.getEnvironment().getProperty(MAX_ROWS_PROPERTY, Integer.TYPE);
        return maxRows > 0;
    }

}