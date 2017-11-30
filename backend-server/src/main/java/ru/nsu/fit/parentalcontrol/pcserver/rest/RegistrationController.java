package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.AuthRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/rest")
public class RegistrationController {

  private static final Logger LOG = Logger.getRootLogger();

  private static final String AUTH_EXISTS = "User with given email already exists";

  @Autowired
  private AuthRepository authRepository;

  @Autowired
  private SecurityService securityService;

  @PostMapping(path = "/registration")
  public ResponseEntity<?> registration(@RequestBody Auth auth) throws URISyntaxException {
    if (authRepository.findById(auth.getEmail()).isPresent())
      throw new RestException(HttpStatus.BAD_REQUEST, AUTH_EXISTS);
    authRepository.save(auth);

    LOG.info(String.format("USER %s REGISTRATION", auth.getEmail()));

    securityService.autoLogin(auth.getEmail(), auth.getPassword());

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(new URI("/user?email=" + auth.getEmail()));

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }
}
