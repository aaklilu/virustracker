package et.bots.thevirustrackerbot.receptor.telegram.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class KeyboardButton {
    private String text;

    @JsonProperty(value = "request_contact", defaultValue = "false")
    @Builder.Default
    private Boolean requestContact=false;

    @JsonProperty("request_location")
    @Builder.Default
    private Boolean requestLocation=false;

}
