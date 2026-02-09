package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "friendship")
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("senderId")
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiverId")
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    public Friendship(User sender, User receiver) {
        this(new FriendshipId(sender.getId(), receiver.getId()), sender, receiver);
    }

    private Friendship(FriendshipId id, User sender, User receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }
}
