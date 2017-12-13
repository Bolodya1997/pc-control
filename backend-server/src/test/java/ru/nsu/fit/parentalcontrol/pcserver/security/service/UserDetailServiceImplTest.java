package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nsu.fit.parentalcontrol.pcserver.model.Admin;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.AdminRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserDetailServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private AdminRepository adminRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  private final String userEmail = "user@host";
  private final String adminEmail = "admin@host";

  private final String fakeEmail = "fake@host";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    final User user = new User(userEmail, "");
    final User adminUser = new User(adminEmail, "");

    final Admin admin = new Admin(adminUser);

    when(userRepository.findByEmail(userEmail))
        .thenReturn(Optional.of(user));
    when(userRepository.findByEmail(adminEmail))
        .thenReturn(Optional.of(adminUser));
    when(userRepository.findByEmail(argThat(s -> s != userEmail && s != adminEmail)))
        .thenReturn(Optional.empty());

    when(adminRepository.findByUser(adminUser))
        .thenReturn(Optional.of(admin));
    when(adminRepository.findByUser(argThat(u -> u != adminUser)))
        .thenReturn(Optional.empty());
  }

  @Test
  public void userTest() {
    final UserDetails details = userDetailsService.loadUserByUsername(userEmail);

    assertThat(details.getUsername(), is(userEmail));
    assertThat(details.getAuthorities(), contains(UserDetailsServiceWithRoles.USER));
  }

  @Test
  public void adminTest() {
    final UserDetails details = userDetailsService.loadUserByUsername(adminEmail);

    assertThat(details.getUsername(), is(adminEmail));
    assertThat(details.getAuthorities(), containsInAnyOrder(UserDetailsServiceWithRoles.USER,
        UserDetailsServiceWithRoles.ADMIN));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void notFoundTest() {
    userDetailsService.loadUserByUsername(fakeEmail);
  }
}
