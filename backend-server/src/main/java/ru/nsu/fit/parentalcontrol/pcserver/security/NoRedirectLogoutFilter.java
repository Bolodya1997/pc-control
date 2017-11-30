package ru.nsu.fit.parentalcontrol.pcserver.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoRedirectLogoutFilter extends LogoutFilter {

  private final static Logger LOG = Logger.getRootLogger();

  public NoRedirectLogoutFilter() {
    super(NoRedirectLogoutFilter::logout, NoRedirectLogoutFilter::logout);
  }

  private static void logout(HttpServletRequest req, HttpServletResponse response,
                             Authentication auth) {
    LOG.info(String.format("USER %s LOGOUT", auth.getPrincipal()));

    response.setStatus(HttpServletResponse.SC_OK);
  }
}
