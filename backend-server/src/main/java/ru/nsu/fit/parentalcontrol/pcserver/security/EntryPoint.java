package ru.nsu.fit.parentalcontrol.pcserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component("entryPoint")
public class EntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse response,
                       AuthenticationException e) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.addHeader(HttpHeaders.LOCATION, "/rest/login");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    final Map<String, String> json = new HashMap<>();
    json.put("error", "Log in");

    final OutputStream out = response.getOutputStream();
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, json);
    out.flush();
  }
}
