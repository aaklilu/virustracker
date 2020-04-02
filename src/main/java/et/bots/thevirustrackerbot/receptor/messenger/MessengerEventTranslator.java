package et.bots.thevirustrackerbot.receptor.messenger;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
public class MessengerEventTranslator implements Processor {

    private final Messenger messenger;

    @Override
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        String signature = exchange.getIn().getHeader(SIGNATURE_HEADER_NAME, String.class);

        log.debug("Translating messenger callback - payload: {} | signature: {}", payload, signature);

        try {
            this.messenger.onReceiveEvents(payload, of(signature), event -> {
                exchange.getMessage().setBody(event);
            });
            log.debug("Translated callback payload successfully");
        } catch (MessengerVerificationException e) {
            log.warn("Translation of callback payload failed: {}", e.getMessage());
            exchange.getMessage().setBody(e);
        }
    }
}