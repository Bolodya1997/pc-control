package ru.nsu.fit.parentalcontrol.pcserver.security.service;

public interface SecurityService {

  String findLoggedInEmail();

  void autoLogin(String email, String password);
}
