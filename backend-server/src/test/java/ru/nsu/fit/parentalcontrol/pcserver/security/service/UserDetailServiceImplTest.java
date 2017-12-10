package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Admin;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.AdminRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.AuthRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserDetailServiceImplTest {

  @Mock
  private AuthRepository authRepository;

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

    final Auth userAuth = new Auth(userEmail, "");
    final Auth adminAuth = new Auth(adminEmail, "");

    final Admin admin = new Admin(adminEmail);

    when(authRepository.findById(eq(userEmail)))
        .thenReturn(Optional.of(userAuth));
    when(authRepository.findById(eq(adminEmail)))
        .thenReturn(Optional.of(adminAuth));
    when(authRepository.findById(argThat(s -> !userEmail.equals(s) && !adminEmail.equals(s))))
        .thenReturn(Optional.empty());

    when(adminRepository.findById(adminEmail))
        .thenReturn(Optional.of(admin));
    when(adminRepository.findById(argThat(s -> !adminEmail.equals(s))))
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
