package greencity.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class FriendshipId implements Serializable {
    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;
}
