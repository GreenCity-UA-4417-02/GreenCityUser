package greencity.security.oauth;

public interface GoogleOAuthStateService {
    String generateAndStore();

    boolean verifyAndConsume(String state);
}
