package et.bots.thevirustrackerbot.receptor.messenger;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import et.bots.thevirustrackerbot.Channel;
import et.bots.thevirustrackerbot.Reaction;
import et.bots.thevirustrackerbot.brain.BotBrain;
import et.bots.thevirustrackerbot.receptor.SubscriberEnricher;
import et.bots.thevirustrackerbot.receptor.SubscriberUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;

import static com.github.messenger4j.Messenger.CHALLENGE_REQUEST_PARAM_NAME;
import static com.github.messenger4j.Messenger.MODE_REQUEST_PARAM_NAME;
import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static com.github.messenger4j.Messenger.VERIFY_TOKEN_REQUEST_PARAM_NAME;

@Slf4j
@RequiredArgsConstructor
public class MessengerRoutes extends RouteBuilder {

    private static final String HEADER_SENDER_ID ="x-sender-id";

    private final BotBrain brain;
    private final SubscriberEnricher subscriberEnricher;
    private final SubscriberUpdater subscriberUpdater;

    private final Messenger messenger;
    private final MessengerEventTranslator messengerEventTranslator;

    @Value("${bot.messages.queue-uri.main}")
    private String messageQueueUri;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .clientRequestValidation(true);

        rest("/messenger").produces("application/json")

                /*
                  Webhook verification endpoint. <p> The passed verification token (as query parameter) must match the configured
                  verification token. In case this is true, the passed challenge string must be returned by this endpoint.
                 */
                .get()
                .param().name(MODE_REQUEST_PARAM_NAME).dataType("string").type(RestParamType.query).required(true).endParam()
                .param().name(VERIFY_TOKEN_REQUEST_PARAM_NAME).dataType("string").type(RestParamType.query).required(true).endParam()
                .param().name(CHALLENGE_REQUEST_PARAM_NAME).dataType("string").type(RestParamType.query).required(true).endParam()
                .route().process(exchange -> {
                    try {
                        String mode = exchange.getIn().getHeader(MODE_REQUEST_PARAM_NAME, String.class);
                        String verifyToken = exchange.getIn().getHeader(VERIFY_TOKEN_REQUEST_PARAM_NAME, String.class);
                        String challenge = exchange.getIn().getHeader(MODE_REQUEST_PARAM_NAME, String.class);

                        this.messenger.verifyWebhook(mode, verifyToken);

                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.SC_OK));
                        exchange.getMessage().setBody(challenge);

                    } catch (MessengerVerificationException e) {
                        log.warn("Webhook verification failed: {}", e.getMessage());

                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.SC_FORBIDDEN));
                        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
                        exchange.getMessage().setBody(constant(e.getMessage()));
                    }
                })
                .endRest()

                /*
                 * Callback endpoint responsible for processing the inbound messages and events.
                 */
                .post()
                .param().name(SIGNATURE_HEADER_NAME).dataType("string").type(RestParamType.query).required(true).endParam()
                .route()
                //Translate the payload into messenger Event
                .process(messengerEventTranslator)
                    //go into a switch and process conditionally
                    .choice()
                        //when there's exception, just return forbidden
                        .when(exchange -> exchange.getMessage().getBody() instanceof Exception)
                            .process(exchange -> {

                                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.SC_FORBIDDEN));
                                exchange.getMessage().setBody(null);
                            })
                        //otherwise go into processing the message
                        .otherwise()
                        .endChoice()
                .end();

        from(messageQueueUri)
                .filter(exchange -> exchange.getIn().getBody(Reaction.class) != null && exchange.getIn().getBody(Reaction.class).getSubscriber().getChannel().equals(Channel.MESSENGER))
                .process(exchange ->{

                    Reaction reaction = exchange.getIn().getBody(Reaction.class);

                    exchange.getIn().setHeader(HEADER_SENDER_ID,  reaction.getSubscriber().getSubscriptionId());
                })

                //Enrich the incoming stimuli with subscriber data
                .process(subscriberEnricher)
                //send to brain for processing
                .bean(brain)
                //update the subscriber
                .process(subscriberUpdater)
                .process(exchange -> {})

                .process();
    }
}
