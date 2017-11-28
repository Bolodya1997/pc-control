package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
public class InnerController {

  @GetMapping("/access-denied")
  public void accessDenied() {
    throw new RestException(HttpStatus.UNAUTHORIZED, "Log in");
  }
}
