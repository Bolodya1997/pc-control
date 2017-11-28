package ru.nsu.fit.parentalcontrol.pcserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Component
public class JSONFilter extends UsernamePasswordAuthenticationFilter {

  private Auth auth;

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
      auth = mapper.readValue(sb.toString(), Auth.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return super.attemptAuthentication(request, response);
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    if (auth != null)
      return auth.getPassword();
    return super.obtainPassword(request);
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    if (auth != null)
      return auth.getEmail();
    return super.obtainUsername(request);
  }

  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }
}
