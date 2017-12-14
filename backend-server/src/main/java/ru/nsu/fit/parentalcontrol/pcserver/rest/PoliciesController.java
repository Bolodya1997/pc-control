package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.parentalcontrol.pcserver.model.Policy;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.PolicyRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;
import ru.nsu.fit.parentalcontrol.pcserver.util.DateFormatter;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/user/policies")
public class PoliciesController {

  private static final String PREFIX = "/rest/user/policies/";

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PolicyRepository policyRepository;

  @PostMapping
  @Transactional
  public ResponseEntity<?> post() throws URISyntaxException {
    final User user = findLoggedInUser();

    Policy policy = new Policy();
    policy.setUser(user);
    policy = policyRepository.save(policy);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(new URI("/rest/user/policies/" + policy.getId()));

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @GetMapping(path = "{id}")
  @Transactional(readOnly = true)
  public ResponseEntity<?> get(@PathVariable Integer id) {
    final Policy policy = findPolicyByIdSafe(id);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLastModified(policy.getLastModified().getTime());

    final Map content = Map.of(
        "sites", PREFIX + id + "/sites",
        "applications", PREFIX + id + "/applications",
        "schedule", PREFIX + id + "/schedule",
        "lastModified", DateFormatter.dateToString(policy.getLastModified()));

    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @DeleteMapping(path = "{id}")
  @Transactional
  public void delete(@PathVariable Integer id) {
    final Policy policy = findPolicyByIdSafe(id);

    policyRepository.delete(policy);
  }

  @GetMapping(path = "{id}/sites")
  public ResponseEntity<?> getSites(@PathVariable Integer id) {
    final Policy policy = findPolicyByIdSafe(id);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLastModified(policy.getLastModified().getTime());

    final Map content = Map.of(
        "sites", policy.getSites(),
        "lastModified", DateFormatter.dateToString(policy.getLastModified()));

    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @PutMapping(path = "{id}/sites")
  @Transactional
  public ResponseEntity<?> putSites(@PathVariable Integer id,
                                    @RequestBody Map<String, String> json) throws ParseException {
    final Policy policy = findPolicyByIdSafe(id);
    policy.setSites(json.get("sites"));
    policy.setLastModified(DateFormatter.stringToData(json.get("lastModified")));
    policyRepository.save(policy);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(PREFIX + id + "/sites"));

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }

  @GetMapping(path = "{id}/applications")
  public ResponseEntity<?> getApplications(@PathVariable Integer id) {
    final Policy policy = findPolicyByIdSafe(id);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLastModified(policy.getLastModified().getTime());

    final Map content = Map.of(
        "applications", policy.getApplications(),
        "lastModified", DateFormatter.dateToString(policy.getLastModified()));

    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @PutMapping(path = "{id}/applications")
  @Transactional
  public ResponseEntity<?> putApplications(@PathVariable Integer id,
                                           @RequestBody Map<String, String> json)
      throws ParseException {
    final Policy policy = findPolicyByIdSafe(id);
    policy.setApplications(json.get("applications"));
    policy.setLastModified(DateFormatter.stringToData(json.get("lastModified")));
    policyRepository.save(policy);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(PREFIX + id + "/applications"));

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }

  @GetMapping(path = "{id}/schedule")
  public ResponseEntity<?> getSchedule(@PathVariable Integer id) {
    final Policy policy = findPolicyByIdSafe(id);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLastModified(policy.getLastModified().getTime());

    final Map content = Map.of(
        "schedule", policy.getSchedule(),
        "lastModified", DateFormatter.dateToString(policy.getLastModified()));

    return new ResponseEntity<>(content, headers, HttpStatus.OK);
  }

  @PutMapping(path = "{id}/schedule")
  @Transactional
  public ResponseEntity<?> putSchedule(@PathVariable Integer id,
                                       @RequestBody Map<String, String> json)
      throws ParseException {
    final Policy policy = findPolicyByIdSafe(id);
    policy.setSchedule(json.get("schedule"));
    policy.setLastModified(DateFormatter.stringToData(json.get("lastModified")));
    policyRepository.save(policy);

    final HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(PREFIX + id + "/schedule"));

    return new ResponseEntity<>(headers, HttpStatus.OK);
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
}
