package et.bots.thevirustrackerbot.subscriber;


import et.bots.thevirustrackerbot.Channel;
import et.bots.thevirustrackerbot.Flow;
import et.bots.thevirustrackerbot.State;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@SuperBuilder
@Table(indexes = {
        @Index(name = "subscriber_idx_subscription_id", columnList = "subscriptionId")
})
@NoArgsConstructor
public class Subscriber {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    protected UUID id;

    @Version
    @Column(columnDefinition = "int Default 1")
    @Builder.Default
    private int version=1;

    private String subscriptionId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String countryCode;
    private String languageCode;

    private Long latitude;
    private Long longitude;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Flow currentFlow = Flow.START;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private State currentState = State.LANDING;
    @Enumerated(EnumType.STRING)
    private Channel channel;
}
