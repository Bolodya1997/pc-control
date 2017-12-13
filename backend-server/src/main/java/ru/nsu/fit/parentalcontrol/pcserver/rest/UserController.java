package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.parentalcontrol.pcserver.model.Child;
import ru.nsu.fit.parentalcontrol.pcserver.model.Policy;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;
import ru.nsu.fit.parentalcontrol.pcserver.util.DateFormatter;

import java.util.Map;

@RestController
@RequestMapping(path = "/rest")
public class UserController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserRepository userRepository;

  @GetMapping(path = "/user")
  public Object get() {
    final String email = securityService.findLoggedInEmail();
    final User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found"));

    final Map subscription = Map.of(
        "type", user.getActualSubscription().getType().getName(),
        "starts", DateFormatter.dateToString(user.getActualSubscription().getStarts()),
        "expires", DateFormatter.dateToString(user.getActualSubscription().getExpires()));
    final int[] children = user.getChildren().stream()
        .mapToInt(Child::getId)
        .toArray();
    final int[] policies = user.getPolicies().stream()
        .mapToInt(Policy::getId)
        .toArray();

    return Map.of(
        "email", email,
        "subscription", subscription,
        "children", children,
        "policies", policies);
  }
}
