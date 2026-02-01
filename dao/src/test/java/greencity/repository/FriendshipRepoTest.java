package greencity.repository;

import greencity.entity.Friendship;
import greencity.entity.User;
import greencity.enums.FriendshipStatus;
import greencity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FriendshipRepoTest {

    @Autowired
    private FriendshipRepo friendshipRepo;

    @Autowired
    private UserRepo userRepo;

    private User sender;
    private User receiver;

    @BeforeEach
    void setup() {
        sender = User.builder()
                .name("Test1")
                .email("test1@gmail.com")
                .role(Role.ROLE_USER)
                .dateOfRegistration(java.time.LocalDateTime.now())
                .refreshTokenKey("token123")
                .build();

        receiver = User.builder()
                .name("Test2")
                .email("test2@gmail.com")
                .role(Role.ROLE_USER)
                .dateOfRegistration(java.time.LocalDateTime.now())
                .refreshTokenKey("token456")
                .build();

        userRepo.save(sender);
        userRepo.save(receiver);
    }

    @Test
    void testSaveFriendship() {
        Friendship friendship = new Friendship();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);

        Friendship saved = friendshipRepo.save(friendship);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(FriendshipStatus.PENDING);
        assertThat(saved.getSender()).isEqualTo(sender);
        assertThat(saved.getReceiver()).isEqualTo(receiver);
    }

    @Test
    void testFindById() {
        Friendship friendship = new Friendship(1L, sender, receiver, FriendshipStatus.ACCEPTED);
        Friendship saved = friendshipRepo.save(friendship);

        Optional<Friendship> found = friendshipRepo.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
    }

    @Test
    void testUniqueConstraint() {
        Friendship friendship1 = new Friendship(2L, sender, receiver, FriendshipStatus.PENDING);
        friendshipRepo.save(friendship1);

        Friendship friendship2 = new Friendship(3L, sender, receiver, FriendshipStatus.ACCEPTED);

        assertThrows(DataIntegrityViolationException.class, () -> friendshipRepo.saveAndFlush(friendship2));
    }

    @Test
    void testDeleteFriendship() {
        Friendship friendship = new Friendship(4L, sender, receiver, FriendshipStatus.PENDING);
        Friendship saved = friendshipRepo.save(friendship);

        friendshipRepo.delete(saved);

        Optional<Friendship> found = friendshipRepo.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
