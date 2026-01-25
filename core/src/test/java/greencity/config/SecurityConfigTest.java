package greencity.config;

import greencity.UserApplication;
import greencity.security.jwt.JwtTool;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityConfigTest.TestController.class)
@ContextConfiguration(classes = UserApplication.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtTool jwtTool;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationConfiguration authenticationConfiguration;

    @MockBean
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager())
            .thenReturn(mock(AuthenticationManager.class));
    }

    @Test
    void withoutAuth_returns401() throws Exception {
        mockMvc.perform(post("/email/sendHabitNotification")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"a@b.com\",\"name\":\"x\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void withRoleUser_not401or403() throws Exception {
        mockMvc.perform(post("/email/sendHabitNotification"))
            .andExpect(result -> {
                int s = result.getResponse().getStatus();
                if (s == 401 || s == 403) {
                    throw new AssertionError("Expected not 401/403 but was " + s);
                }
            });
    }

    @RestController
    static class TestController {
        @PostMapping("/email/sendHabitNotification")
        ResponseEntity<Void> ok() {
            return ResponseEntity.ok().build();
        }
    }
}
