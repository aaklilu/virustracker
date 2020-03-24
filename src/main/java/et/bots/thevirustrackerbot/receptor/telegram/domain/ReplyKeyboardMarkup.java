package et.bots.thevirustrackerbot.receptor.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Boolean removeKeyboard=false;

    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard=false;

    @JsonProperty("selective")
    private Boolean selective=false;

    private List<List<KeyboardButton>> keyboard;
}
