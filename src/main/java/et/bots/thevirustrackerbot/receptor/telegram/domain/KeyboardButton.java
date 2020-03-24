package et.bots.thevirustrackerbot.receptor.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class KeyboardButton {
    private String text;

    @JsonProperty(value = "request_contact", defaultValue = "false")
    private Boolean requestContact=false;

    @JsonProperty("request_location")
    private Boolean requestLocation=false;

}
