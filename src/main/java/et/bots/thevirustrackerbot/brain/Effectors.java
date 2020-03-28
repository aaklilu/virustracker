package et.bots.thevirustrackerbot.brain;

import et.bots.thevirustrackerbot.Flow;
import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.State;
import et.bots.thevirustrackerbot.event.v1.UpdateRequestedEvent;
import et.bots.thevirustrackerbot.subscriber.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class Effectors {

    private final ProducerTemplate eventProducerTemplate;
    private final MessageSource messageSource;
    private static Map<String, String> countries = new HashMap<>();

    static {
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
    }

    @EffectorMapping(stimulus = "^(start|/start|hi|hello)")
    public Reaction start(Stimuli stimuli){

        try {
            Subscriber subscriber = stimuli.getSubscriber();
            subscriber.setCurrentFlow(Flow.START);
            subscriber.setCurrentState(State.COUNTRY);


            return Reaction
                    .builder()
                    .subscriber(stimuli.getSubscriber())
                    .text(messageSource.getMessage("flow.start.greet", new Object[]{stimuli.getSubscriber().getFirstName()}, LocaleContextHolder.getLocale()))
                    .optionVector(getCountryOptions())
                    .build();

        }catch (Exception e){
            log.error("Error {}", e.getMessage(),e);
            return Reaction.builder().text(messageSource.getMessage("error.sick", new Object[]{}, LocaleContextHolder.getLocale())).build();
        }
    }

    @EffectorMapping(flow = Flow.START, state = State.COUNTRY, stimulus = "^\\S*")
    public Reaction country(Stimuli stimuli){

        String countryCode=countries.get(stimuli.getMessage()) != null? countries.get(stimuli.getMessage()).toUpperCase(): stimuli.getMessage().toUpperCase();
        if(StringUtils.isEmpty(countryCode)){

            return Reaction
                    .builder()
                    .text(messageSource.getMessage("error.unknown_country", new Object[]{}, LocaleContextHolder.getLocale()))
                    .optionVector(getCountryOptions())
                    .build();
        }

        Subscriber subscriber = stimuli.getSubscriber();
        subscriber.setCountryCode(countryCode);
        subscriber.setCurrentFlow(Flow.TRACK);
        subscriber.setCurrentState(State.TRACK);

        eventProducerTemplate.sendBody(UpdateRequestedEvent
                .builder()
                .to(subscriber.getSubscriptionId())
                .countryCode(subscriber.getCountryCode())
                .build()
        );
        
        return Reaction
                .builder()
                .subscriber(subscriber)
                .text(messageSource.getMessage("message.subscribed", new Object[]{}, LocaleContextHolder.getLocale()))
                .optionVector(Collections.singletonList(Collections.singletonList(Reaction.Option
                        .builder()
                        .callbackData("refresh")
                        .text(messageSource.getMessage("button.refresh", new Object[]{}, LocaleContextHolder.getLocale()))
                        .build())))
                .build();
    }

    private List<List<Reaction.Option>> getCountryOptions(){

        List<List<Reaction.Option>> optionVector = new ArrayList<>();
        optionVector.add(Collections.singletonList(Reaction.Option
                .builder()
                .text(messageSource.getMessage("button.country.et", new Object[]{}, LocaleContextHolder.getLocale()))
                .callbackData("Ethiopia")
                .build()));

        optionVector.add(Collections.singletonList(Reaction.Option
                .builder()
                .text(messageSource.getMessage("button.country.ng", new Object[]{}, LocaleContextHolder.getLocale()))
                .callbackData("Nigeria")
                .build()));

        return optionVector;
    }


    @EffectorMapping(flow = Flow.TRACK, state = State.TRACK, stimulus = "^\\S*")
    public Reaction update(Stimuli stimuli){

        Subscriber subscriber = stimuli.getSubscriber();

        eventProducerTemplate.sendBody(UpdateRequestedEvent
                .builder()
                .to(subscriber.getSubscriptionId())
                .countryCode(subscriber.getCountryCode())
                .build()
        );
        return null;
    }

    @EffectorMapping(stimulus = "^(help)")
    public Reaction help(Stimuli stimuli){
        return Reaction
                .builder()
                .text(messageSource.getMessage("flow.start.greet", new Object[]{stimuli.getSubscriber().getFirstName()}, LocaleContextHolder.getLocale()))
                .build();
    }
}
