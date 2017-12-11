package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class SecurityServiceImplTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private SecurityContext securityContext;

  @InjectMocks
  private SecurityServiceImpl securityService;

  private final String email = "user@host";
  private final String fakeEmail = "fake@host";

  private final Set<GrantedAuthority> authorities = Set.of(UserDetailsServiceWithRoles.USER);

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(userDetailsService.loadUserByUsername(eq(email)))
        .thenReturn(new User(email, fakeEmail, authorities));
    when(userDetailsService.loadUserByUsername(argThat(s -> !email.equals(s))))
        .thenThrow(new UsernameNotFoundException(""));

    SecurityContextHolder.setContext(securityContext);
    doNothing().when(securityContext).setAuthentication(any());
  }

  @Test
  public void correctEmailPasswordTest() {
    when(authenticationManager.authenticate(any()))
        .then(invocation -> invocation.getArgument(0));

    securityService.autoLogin(email, "");

    verify(securityContext, times(1)).setAuthentication(any());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void fakeEmailTest() {
    securityService.autoLogin(fakeEmail, "");
  }

  @Test
  public void fakePasswordTest() {
    when(authenticationManager.authenticate(any()))
        .then(invocation -> {
          final Authentication auth = invocation.getArgument(0);
          auth.setAuthenticated(false);
          return auth;
        });

    securityService.autoLogin(email, "");

    verify(securityContext, times(0)).setAuthentication(any());
  }

  @Test
  public void noLoggedInTest() {
    when(securityContext.getAuthentication())
        .thenReturn(null);

    final String __email = securityService.findLoggedInEmail();

    assertThat(__email, is(nullValue()));
  }

  @Test
  public void loggedInTest() {
    final UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(email, "", authorities);
    token.setDetails(new User(email, "", authorities));

    when(securityContext.getAuthentication())
        .thenReturn(token);

    final String __email = securityService.findLoggedInEmail();

    assertThat(__email, is(email));
  }
}
