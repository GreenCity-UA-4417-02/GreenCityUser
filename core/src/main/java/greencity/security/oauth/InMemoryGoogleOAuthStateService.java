package greencity.security.oauth;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class InMemoryGoogleOAuthStateService implements GoogleOAuthStateService {
    private static final Duration TTL = Duration.ofMinutes(10);

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Instant> states = new ConcurrentHashMap<>();
    private final Clock clock = Clock.systemUTC();

    @Override
    public String generateAndStore() {
        cleanupExpired();

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String state = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        states.put(state, Instant.now(clock).plus(TTL));
        return state;
    }

    @Override
    public boolean verifyAndConsume(String state) {
        cleanupExpired();

        Instant expiresAt = states.remove(state);
        return expiresAt != null && expiresAt.isAfter(Instant.now(clock));
    }

    private void cleanupExpired() {
        Instant now = Instant.now(clock);
        states.entrySet().removeIf(e -> e.getValue().isBefore(now));
    }
}
