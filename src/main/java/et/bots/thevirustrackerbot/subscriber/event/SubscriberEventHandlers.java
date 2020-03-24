package et.bots.thevirustrackerbot.subscriber.event;

import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.event.v1.StatsUpdatedEvent;
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

    @Consume(property = "eventEndpointUrl", predicate = "${body} is 'com.paga.thevirustrackerbot.event.v1.StatsUpdatedEvent'")
    public CompletableFuture<Void> handleStatsUpdatedEvent(StatsUpdatedEvent event){

        List<List<Reaction.Option>> optionVector = new ArrayList<>();
        optionVector.add(Collections.singletonList(Reaction.Option
                .builder()
                .callbackData("refresh")
                .text(messageSource.getMessage("button.refresh", new Object[]{}, LocaleContextHolder.getLocale()))
                .build()));

        String text = messageSource.getMessage("status.update.title", new Object[]{new Locale("", event.getStatsDTO().getCountryCode().toUpperCase()).getDisplayCountry()}, LocaleContextHolder.getLocale()) +
                "\r\n" +
                messageSource.getMessage("status.update.section.new", new Object[]{}, LocaleContextHolder.getLocale()) +
                "\r\n" +
                messageSource.getMessage("status.update.confirmed", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getNewCases() +
                "\r\n" +
                messageSource.getMessage("status.update.deaths", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getNewDeaths() +
                "\r\n" +
                messageSource.getMessage("status.update.recovered", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getNewRecoveries() +
                "\r\n" +
                "\r\n" +
                messageSource.getMessage("status.update.section.total", new Object[]{}, LocaleContextHolder.getLocale()) +
                "\r\n" +
                messageSource.getMessage("status.update.confirmed", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getTotalCases() +
                "\r\n" +
                messageSource.getMessage("status.update.deaths", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getTotalDeaths() +
                "\r\n" +
                messageSource.getMessage("status.update.recovered", new Object[]{}, LocaleContextHolder.getLocale())+ ": " + event.getStatsDTO().getTotalRecovered();

        if(!StringUtils.isEmpty(event.getSubscriberId())){
            subscriberService.findBySubscriptionId(event.getSubscriberId()).ifPresent(subscriber -> messageQueueProducerTemplate.sendBody(messageQueueUrl, Reaction.builder().subscriber(subscriber).text(text).optionVector(optionVector).build()));
        }else {
            subscriberService.findAll().forEach(subscriber -> messageQueueProducerTemplate.sendBody(messageQueueUrl, Reaction.builder().subscriber(subscriber).text(text).optionVector(optionVector).build()));
        }

        return CompletableFuture.completedFuture(null);
    }

}
