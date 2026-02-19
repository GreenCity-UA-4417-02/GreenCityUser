package greencity.security.controller;

import greencity.config.GoogleOAuthProperties;
import greencity.security.oauth.GoogleOAuthService;
import greencity.security.oauth.InMemoryGoogleOAuthStateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GoogleOAuthRedirectTest {
    @Test
    void getAuthGoogle_shouldReturn302WithValidUrlAndState() throws Exception {
        GoogleOAuthProperties props = new GoogleOAuthProperties();

        GoogleOAuthService googleOAuthService = Mockito.mock(GoogleOAuthService.class);

        MockMvcBuilders.standaloneSetup(
            new GoogleOAuthCallbackController(props, new InMemoryGoogleOAuthStateService(), googleOAuthService))
            .build();

        props.setClientId("client-id-123");
        props.setClientSecret("secret");
        props.setRedirectUri("http://localhost:8080/auth/google/callback");

        InMemoryGoogleOAuthStateService stateService = new InMemoryGoogleOAuthStateService();
        GoogleOAuthCallbackController controller = new GoogleOAuthCallbackController(props, stateService,
            googleOAuthService);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        var result = mvc.perform(get("/auth/google"))
            .andExpect(status().isFound())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("https://accounts.google.com/o/oauth2/v2/auth?"));
        assertTrue(location.contains("client_id=client-id-123"));
        assertTrue(location.contains("redirect_uri=http://localhost:8080/auth/google/callback"));
        assertTrue(location.contains("scope=email%20profile"));
        assertTrue(location.contains("response_type=code"));
        assertTrue(location.contains("state="));

        String state = location.substring(location.indexOf("state=") + "state=".length());
        if (state.contains("&")) {
            state = state.substring(0, state.indexOf("&"));
        }

        assertTrue(stateService.verifyAndConsume(state));
        assertFalse(stateService.verifyAndConsume(state));
    }
}
