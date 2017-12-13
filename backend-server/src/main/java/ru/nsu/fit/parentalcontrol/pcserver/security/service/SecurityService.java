package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {

  String findLoggedInEmail();

  void autoLogin(String email, String password);

  void logout(HttpServletRequest request);
}
