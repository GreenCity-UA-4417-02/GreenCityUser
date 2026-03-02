package greencity.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "friendships")
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
}
