package greencity.repository;

import greencity.entity.Friendship;
import greencity.entity.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepo extends JpaRepository<Friendship, FriendshipId> {

    @Query("""
           SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id
                    ELSE f.user1.id
                  END
           FROM Friendship f
           WHERE f.user1.id = :userId
              OR f.user2.id = :userId
       """)
    List<Long> findFriendIdsByUserId(Long userId);
}
