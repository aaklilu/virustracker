package et.bots.thevirustrackerbot.receptor.telegram;

import et.bots.thevirustrackerbot.Reaction;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.*;

import java.util.stream.Collectors;

public class TelegramReactionTranslator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        Reaction reaction = exchange.getIn().getBody(Reaction.class);

        ReplyMarkup replyKeyboardMarkup = null;

        if(reaction != null) {
            if (reaction.getOptionVector() != null && !reaction.getOptionVector().isEmpty()) {

                if(reaction.isInline()){
                    replyKeyboardMarkup = InlineKeyboardMarkup
                            .builder()
                            .build();

                    ((InlineKeyboardMarkup)replyKeyboardMarkup)
                            .setInlineKeyboard(reaction
                                    .getOptionVector()
                                    .stream()
                                    .map(options -> options
                                            .stream()
                                            .map(option -> InlineKeyboardButton
                                                    .builder()
                                                    .text(option.getText())
                                                    .callbackData(option.getCallbackData())
                                                    .build())
                                            .collect(Collectors.toList()))
                                    .collect(Collectors.toList()));
                }else {
                    replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                            .oneTimeKeyboard(true)
                            .removeKeyboard(true)
                            .resizeKeyboard(false)
                            .selective(false)
                            .build();

                    ((ReplyKeyboardMarkup)replyKeyboardMarkup)
                            .setKeyboard(reaction
                                    .getOptionVector()
                                    .stream()
                                    .map(options -> options
                                            .stream()
                                            .map(option -> InlineKeyboardButton
                                                    .builder()
                                                    .text(option.getText())
                                                    .callbackData(option.getCallbackData())
                                                    .build())
                                            .collect(Collectors.toList()))
                                    .collect(Collectors.toList()));
                }
            }

            exchange.getIn().setBody(OutgoingTextMessage
                    .builder()
                    .text(reaction.getText())
                    .replyMarkup(replyKeyboardMarkup)
                    .build());
        }else {
            exchange.getIn().setBody(null);//make sure nothing gets sent out...just in case
        }
    }
}
