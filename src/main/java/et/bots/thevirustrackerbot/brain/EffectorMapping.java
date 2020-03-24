package et.bots.thevirustrackerbot.brain;

import et.bots.thevirustrackerbot.Flow;
import et.bots.thevirustrackerbot.State;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EffectorMapping {

    Flow flow() default Flow.START;
    State state() default State.LANDING;
    String stimulus();
}