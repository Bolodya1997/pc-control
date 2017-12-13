package ru.nsu.fit.parentalcontrol.pcserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

@Component
public class JSONAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final Logger LOG = Logger.getRootLogger();

  private User user;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException {
    try {
      final StringBuffer sb = new StringBuffer();
      String line;

      final BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null){
        sb.append(line);
      }

      final ObjectMapper mapper = new ObjectMapper();
      user = mapper.readValue(sb.toString(), User.class);
    } catch (Exception e) {
      LOG.warn("", e);
    }

    return super.attemptAuthentication(request, response);
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    if (user != null)
      return user.getPassword();
    return super.obtainPassword(request);
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    if (user != null)
      return user.getEmail();
    return super.obtainUsername(request);
  }

  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }
}
