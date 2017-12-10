package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public String findLoggedInEmail() {
    final Authentication token = SecurityContextHolder.getContext().getAuthentication();
    if (token == null)
      return null;

    final Object userDetails = token.getDetails();
    if (userDetails instanceof UserDetails)
      return ((UserDetails) userDetails).getUsername();

    return null;
  }

  @Override
  public void autoLogin(String email, String password) {
    final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    final UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(userDetails, password,
            userDetails.getAuthorities());

    authenticationManager.authenticate(token);

    if (token.isAuthenticated())
      SecurityContextHolder.getContext().setAuthentication(token);
  }
}
