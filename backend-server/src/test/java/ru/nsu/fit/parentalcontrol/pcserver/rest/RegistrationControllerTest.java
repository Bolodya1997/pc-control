package ru.nsu.fit.parentalcontrol.pcserver.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class RegistrationControllerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityService securityService;

  @InjectMocks
  private RegistrationController registrationController;

  private MockMvc mvc;

  final String email = "user@host";
  final String password = "password";

  final String newEmail = "new@host";

  final User registered = new User(email, password);
  final User notRegistered = new User(newEmail, password);

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mvc = MockMvcBuilders.standaloneSetup(registrationController)
        .setControllerAdvice(new ControllerExceptionHandler())
        .build();

    final User user = new User(email, password);

    when(userRepository.findByEmail(eq(email)))
        .thenReturn(Optional.of(user));
    when(userRepository.findByEmail(argThat(s -> !email.equals(s))))
        .thenReturn(Optional.empty());

    doNothing().when(securityService).autoLogin(any(), any());
  }

  @Test
  public void registrationSuccess() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();

    mvc.perform(post("/rest/registration")
          .content(mapper.writeValueAsBytes(notRegistered))
          .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.LOCATION, "/rest/user"));

    verify(securityService, times(1)).autoLogin(eq(newEmail), eq(password));
  }

  @Test
  public void registrationFailure() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();

    mvc.perform(post("/rest/registration")
        .content(mapper.writeValueAsBytes(registered))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verify(securityService, times(0)).autoLogin(eq(newEmail), eq(password));
  }
}