package et.bots.thevirustrackerbot.subscriber.event;

import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.StatsDTO;
import et.bots.thevirustrackerbot.event.v1.StatsUpdateFailedEvent;
import et.bots.thevirustrackerbot.event.v1.StatsUpdatedEvent;
import et.bots.thevirustrackerbot.subscriber.Subscriber;
import et.bots.thevirustrackerbot.subscriber.SubscriberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consume;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class SubscriberEventHandlers {

    private final SubscriberService subscriberService;
    private final ProducerTemplate messageQueueProducerTemplate;
    private final MessageSource messageSource;

    @Getter
    @Value("${bot.events.queue-uri.main}")
    private String eventEndpointUrl;

    @Getter
    @Value("${bot.messages.queue-uri.main}")
    private String messageQueueUrl;

    @Consume(property = "eventEndpointUrl", predicate = "${body} is 'et.bots.thevirustrackerbot.event.v1.StatsUpdatedEvent'")
    public CompletableFuture<Void> handleStatsUpdatedEvent(StatsUpdatedEvent event){

        if(!StringUtils.isEmpty(event.getSubscriberId())){

            subscriberService.findBySubscriptionId(event.getSubscriberId()).ifPresent(subscriber -> {
                messageQueueProducerTemplate.sendBody(messageQueueUrl, buildReaction("status.update.title", subscriber, event.getStatsDTO()));
            });
        }else {
            subscriberService.findByCountryCode(event.getStatsDTO().getCountryCode()).forEach(subscriber -> {

                messageQueueProducerTemplate.sendBody(messageQueueUrl, buildReaction("status.update.title", subscriber, event.getStatsDTO()));
            });
        }

        return CompletableFuture.completedFuture(null);
    }

    @Consume(property = "eventEndpointUrl", predicate = "${body} is 'et.bots.thevirustrackerbot.event.v1.StatsUpdateFailedEvent'")
    public CompletableFuture<Void> handleStatsUpdateFailedEvent(StatsUpdateFailedEvent event){

        if(!StringUtils.isEmpty(event.getSubscriberId())){

            subscriberService.findBySubscriptionId(event.getSubscriberId()).ifPresent(subscriber -> {
                messageQueueProducerTemplate.sendBody(messageQueueUrl, buildReaction(event.getStatsDTO()!= null? "status.update.error.title": "status.update.error.no-stats.title", subscriber, event.getStatsDTO()));
            });
        }

        return CompletableFuture.completedFuture(null);
    }

    private Reaction buildReaction(String titleKey, Subscriber subscriber, StatsDTO statsDTO){

        List<List<Reaction.Option>> optionVector = new ArrayList<>();
        optionVector.add(Collections.singletonList(Reaction.Option
                .builder()
                .callbackData("refresh")
                .text(messageSource.getMessage("button.refresh", new Object[]{}, LocaleContextHolder.getLocale()))
                .build()));

        String text = messageSource.getMessage(titleKey, new Object[]{new Locale("", subscriber.getCountryCode().toUpperCase()).getDisplayCountry()}, LocaleContextHolder.getLocale());

                if(statsDTO != null) {
                   text += "\r\n" +
                            messageSource.getMessage("status.update.section.new", new Object[]{}, LocaleContextHolder.getLocale()) +
                            "\r\n" +
                            messageSource.getMessage("status.update.confirmed", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getNewCases() +
                            "\r\n" +
                            messageSource.getMessage("status.update.deaths", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getNewDeaths() +
                            "\r\n" +
                            messageSource.getMessage("status.update.recovered", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getNewRecoveries() +
                            "\r\n" +
                            "\r\n" +
                            messageSource.getMessage("status.update.section.total", new Object[]{}, LocaleContextHolder.getLocale()) +
                            "\r\n" +
                            messageSource.getMessage("status.update.confirmed", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getTotalCases() +
                            "\r\n" +
                            messageSource.getMessage("status.update.deaths", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getTotalDeaths() +
                            "\r\n" +
                            messageSource.getMessage("status.update.recovered", new Object[]{}, LocaleContextHolder.getLocale()) + ": " + statsDTO.getTotalRecovered();
                }

        return Reaction.builder().text(text).subscriber(subscriber).optionVector(optionVector).build();
    }

}
