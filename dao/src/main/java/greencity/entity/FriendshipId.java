package greencity.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Embeddable
public class FriendshipId implements Serializable {
    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    public FriendshipId(Long id1, Long id2) {
        if (id1.equals(id2)) {
            throw new IllegalArgumentException("User cannot be friend with himself");
        }

        if (id1 < id2) {
            this.user1Id = id1;
            this.user2Id = id2;
        } else {
            this.user1Id = id2;
            this.user2Id = id1;
        }
    }
}
