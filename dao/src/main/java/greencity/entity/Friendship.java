package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "friendship")
@ToString
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user1Id")
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user2Id")
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public Friendship(User user1, User user2) {
        this(new FriendshipId(user1.getId(), user2.getId()), user1, user2);
    }

    private Friendship(FriendshipId id, User user1, User user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }
}
