package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.parentalcontrol.pcserver.model.Child;
import ru.nsu.fit.parentalcontrol.pcserver.model.Policy;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.ChildRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.PolicyRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/user/children")
public class ChildrenController {

  private static final String PREFIX = "/rest/user/children/";

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PolicyRepository policyRepository;

  @Autowired
  private ChildRepository childRepository;

  @PostMapping
  @Transactional
  public ResponseEntity<?> post(@RequestBody Map<String, String> json) {
    final int policyId = Integer.decode(json.get("policyId"));
    final Policy policy = findPolicyByIdSafe(policyId);

    Child child = new Child();
    child.setUser(policy.getUser());
    child.setPolicy(policy);
    child.setName(json.get("name"));
    child = childRepository.save(child);

    final Map body = Map.of("id", child.getId());

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(PREFIX + child.getId()));

    return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
  }

  @GetMapping(path = "/{id}")
  @Transactional(readOnly = true)
  public Object get(@PathVariable Integer id) {
    final Child child = findChildByIdSafe(id);

    return Map.of(
        "name", child.getName(),
        "policyId", child.getPolicy().getId());
  }

  @PutMapping(path = "/{id}")
  @Transactional
  public ResponseEntity<?> put(@PathVariable Integer id,
                               @RequestBody Map<String, String> json) {
    final int policyId = Integer.decode(json.get("policyId"));
    final Policy policy = findPolicyByIdSafe(policyId);

    final Child child = findChildByIdSafe(id);
    child.setPolicy(policy);
    child.setName(json.get("name"));
    childRepository.save(child);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(PREFIX + id));

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  @Transactional
  public void delete(@PathVariable Integer id) {
    final Child child = findChildByIdSafe(id);

    childRepository.delete(child);
  }

  private User findLoggedInUser() {
    final String email = securityService.findLoggedInEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found"));
  }

  private Policy findPolicyByIdSafe(Integer id) {
    final User user = findLoggedInUser();

    final Optional<Policy> optional = policyRepository.findById(id);
    if (!optional.isPresent() || !user.equals(optional.get().getUser()))
      throw new RestException(HttpStatus.NOT_FOUND, "Policy with id=" + id + " not found");

    return optional.get();
  }

  private Child findChildByIdSafe(Integer id) {
    final User user = findLoggedInUser();

    final Optional<Child> optional = childRepository.findById(id);
    if (!optional.isPresent() || !user.equals(optional.get().getUser()))
      throw new RestException(HttpStatus.NOT_FOUND, "Child with id=" + id + " not found");

    return optional.get();
  }
}
