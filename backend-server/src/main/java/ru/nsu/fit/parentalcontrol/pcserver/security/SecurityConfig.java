package ru.nsu.fit.parentalcontrol.pcserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final Logger LOG = Logger.getRootLogger();

  @Resource
  private EntryPoint entryPoint;

  @Resource
  private NoRedirectLogoutFilter noRedirectLogoutFilter;

  @Resource
  private JSONFilter jsonFilter;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/rest/registration").permitAll()
        .antMatchers("/rest/login").permitAll()
        .anyRequest().authenticated();

    http.exceptionHandling()
        .authenticationEntryPoint(entryPoint);

    http.csrf().disable();

    http.httpBasic();

//    ------   NoRedirectLogoutFilter   ------

    noRedirectLogoutFilter.setLogoutRequestMatcher(
        new RegexRequestMatcher("/rest/logout", "POST"));

    http.addFilter(noRedirectLogoutFilter);

//    ------   JSONFilter   ------

    jsonFilter.setRequiresAuthenticationRequestMatcher(
        new RegexRequestMatcher("/rest/login", "POST"));
    jsonFilter.setAuthenticationFailureHandler(this::authFailure);
    jsonFilter.setAuthenticationSuccessHandler(this::authSuccess);

    http.addFilter(jsonFilter);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  private void authFailure(HttpServletRequest req, HttpServletResponse response,
                           AuthenticationException e) throws IOException {
    LOG.warn("AUTHENTICATION FAILURE");

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.addHeader(HttpHeaders.LOCATION, "/rest/recovery");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    final OutputStream out = response.getOutputStream();
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, Map.of("error", "Invalid email or Password"));
    out.flush();
  }

  private void authSuccess(HttpServletRequest req, HttpServletResponse response,
                           Authentication auth) {
    LOG.info(String.format("USER %s LOGIN", auth.getPrincipal()));

    response.setStatus(HttpServletResponse.SC_OK);
    response.addHeader(HttpHeaders.LOCATION, "/rest/user?email=" + auth.getName());
  }
}
