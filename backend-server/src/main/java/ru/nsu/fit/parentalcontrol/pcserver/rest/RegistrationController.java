package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/rest")
public class RegistrationController {

  private static final Logger LOG = Logger.getRootLogger();

  private static final String USER_EXISTS = "User with given email already exists";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SecurityService securityService;

  @PostMapping(path = "/registration")
  @Transactional
  public ResponseEntity<?> registration(@RequestBody User user) throws URISyntaxException {
    if (userRepository.findByEmail(user.getEmail()).isPresent())
      throw new RestException(HttpStatus.BAD_REQUEST, USER_EXISTS);
    userRepository.save(user);

    LOG.info(String.format("USER %s REGISTRATION", user.getEmail()));

    securityService.autoLogin(user.getEmail(), user.getPassword());

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(new URI("/rest/user"));

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }
}
