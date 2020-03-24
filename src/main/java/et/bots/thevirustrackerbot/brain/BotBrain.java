package et.bots.thevirustrackerbot.brain;

import et.bots.thevirustrackerbot.Flow;
import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Handler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class BotBrain {

    private final ApplicationContext applicationContext;
    private final MessageSource messageSource;

    private List<EffectorMethodWrapper> effectors = new ArrayList<>();

    @PostConstruct
    public void init() {

        List<Method> methods = new ArrayList<>();

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> type = applicationContext.getType(beanName);

            if (type.getPackage() != null && StringUtils.startsWithIgnoreCase(type.getPackage().getName(), "et.bots.thevirustrackerbot.brain")) {
                methods.addAll(Arrays.asList(type.getDeclaredMethods()));
            }

        }

        methods.forEach(this::evaluateMethod);
    }

    private void evaluateMethod(Method method) {

        if (method.isAnnotationPresent(EffectorMapping.class)) {

            EffectorMapping effector = method.getAnnotation(EffectorMapping.class);

            effectors.add(new EffectorMethodWrapper(method, effector.stimulus(), effector.flow(), effector.state()));
        }
    }

    private boolean validateHandlerMethod(EffectorMethodWrapper effector, Stimuli stimuli) {

        return !effector.messagePattern.isEmpty() &&
                stimuli.getSubscriber().getCurrentFlow().equals(effector.flow) &&
                stimuli.getSubscriber().getCurrentState().equals(effector.state) &&
                Pattern.compile(effector.messagePattern).matcher(stimuli.getMessage().toLowerCase()).matches();
    }

    @Handler
    public Reaction dispatch(Stimuli stimuli) {

        log.info("Handling incoming stimuli: {}", stimuli);
        try {

            Optional<EffectorMethodWrapper> matchingMethod = effectors
                    .stream()
                    .filter(handlerMethodWrapper -> validateHandlerMethod(handlerMethodWrapper, stimuli))
                    .findFirst();
            AtomicReference<Reaction> result = new AtomicReference<>(null);
            if(matchingMethod.isPresent()){

                Method method = matchingMethod.get().method;

                try {

                    result.set((Reaction) method.invoke(applicationContext.getBean(method.getDeclaringClass()), stimuli));
                } catch (Exception e) {

                    log.error("Error invoking handler: ", e);
                    result.set(Reaction.builder().text(messageSource.getMessage("error.sick", new Object[]{}, LocaleContextHolder.getLocale())).build());
                }
            }else{

                log.error("Unknown command {} ", stimuli.getMessage());
                result.set(Reaction.builder().text(messageSource.getMessage("error.unknown_command", new Object[]{}, LocaleContextHolder.getLocale())).build());
            }

            return result.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Reaction.builder().text(messageSource.getMessage("error.sick", new Object[]{}, LocaleContextHolder.getLocale())).build();
        }
    }

    private static class EffectorMethodWrapper {

        private final Method method;
        private final String messagePattern;
        private final Flow flow;
        private final State state;

        private EffectorMethodWrapper(Method method, String messagePattern, Flow flow, State state) {
            this.method = method;
            this.messagePattern = messagePattern;
            this.flow = flow;
            this.state = state;
        }
    }
}
