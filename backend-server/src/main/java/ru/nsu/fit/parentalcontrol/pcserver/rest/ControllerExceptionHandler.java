package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  private class Error {
    private final String error;

    private Error(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }
  }

  @ExceptionHandler(RestException.class)
  public ResponseEntity<Error> errorHandler(RestException e) {
    return new ResponseEntity<>(new Error(e.getMessage()), e.getStatus());
  }
}
