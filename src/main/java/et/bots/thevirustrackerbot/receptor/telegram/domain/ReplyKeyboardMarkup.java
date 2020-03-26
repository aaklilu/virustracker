package et.bots.thevirustrackerbot.receptor.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.camel.component.telegram.model.ReplyMarkup;

import java.util.List;

@Data
@SuperBuilder
public class ReplyKeyboardMarkup implements ReplyMarkup {

    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;

    @JsonProperty("remove_keyboard")
    @Builder.Default
    private Boolean removeKeyboard=false;

    @JsonProperty("resize_keyboard")
    @Builder.Default
    private Boolean resizeKeyboard=false;

    @JsonProperty("selective")
    @Builder.Default
    private Boolean selective=false;

    private List<List<KeyboardButton>> keyboard;
}
