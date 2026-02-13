package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "friendship")
@ToString(exclude = {"user1", "user2"})
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
        if (user1.getId().equals(user2.getId())) {
            throw new IllegalArgumentException("User cannot be friend with himself");
        }

        this.id = new FriendshipId(user1.getId(), user2.getId());

        if (user1.getId().equals(id.getUser1Id())) {
            this.user1 = user1;
            this.user2 = user2;
        } else {
            this.user1 = user2;
            this.user2 = user1;
        }
    }
}
