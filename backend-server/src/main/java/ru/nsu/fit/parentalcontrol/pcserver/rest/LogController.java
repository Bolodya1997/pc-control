package ru.nsu.fit.parentalcontrol.pcserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.parentalcontrol.pcserver.model.Child;
import ru.nsu.fit.parentalcontrol.pcserver.model.Log;
import ru.nsu.fit.parentalcontrol.pcserver.model.LogType;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.ChildRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.LogRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.LogTypeRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.service.SecurityService;
import ru.nsu.fit.parentalcontrol.pcserver.util.DateFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/rest/user/log")
public class LogController {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private LogTypeRepository logTypeRepository;

  @Autowired
  private LogRepository logRepository;

  @PostMapping
  @Transactional
  public void post(@RequestBody Map<String, String>[] jsons) throws ParseException {
    final User user = findLoggedInUser();

    for (int i = 0; i < jsons.length; i++) {
      final int childId = Integer.decode(jsons[i].get("childId"));
      final Optional<Child> childOptional = childRepository.findById(childId);
      if (!childOptional.isPresent() || !childOptional.get().getUser().equals(user))
        throw new RestException(HttpStatus.BAD_REQUEST, "Incorrect child in " + (i + 1));

      final Optional<LogType> logTypeOptional = logTypeRepository.findByName(jsons[i].get("type"));
      if (!logTypeOptional.isPresent())
        throw new RestException(HttpStatus.BAD_REQUEST, "Incorrect type in " + (i + 1));

      final Log log = new Log();
      log.setChild(childOptional.get());
      log.setTime(DateFormatter.stringToData(jsons[i].get("time")));
      log.setType(logTypeOptional.get());
      log.setInfo(jsons[i].get("data"));
      logRepository.save(log);
    }
  }

  @GetMapping
  public Object get(@RequestParam(name = "id", required = false) Integer id,
                    @RequestParam(name = "child", required = false) Integer childId,
                    @RequestParam(name = "type", required = false) String type,
                    @RequestParam(name = "from", required = false) String fromISO,
                    @RequestParam(name = "to", required = false) String toISO)
      throws ParseException {
    final User user = findLoggedInUser();
    final Date from = fromISO != null ? DateFormatter.stringToData(fromISO)
                                      : null;
    final Date to = toISO != null ? DateFormatter.stringToData(toISO)
                                  : null;

    return StreamSupport.stream(logRepository.findAll().spliterator(), true)
        .filter(log -> log.getUser().equals(user))
        .filter(log -> id == null || log.getId().equals(id))
        .filter(log -> childId == null || log.getChild().getId().equals(id))
        .filter(log -> type == null || log.getType().getName().equals(type))
        .filter(log -> from == null || log.getTime().after(from))
        .filter(log -> to == null || log.getTime().before(to))
        .map(log -> Map.of(
            "logId", log.getId(),
            "childId", log.getChild().getId(),
            "time", DateFormatter.dateToString(log.getTime()),
            "type", log.getType().getName(),
            "data", log.getInfo()))
        .toArray();
  }

  @DeleteMapping
  @Transactional
  public void delete(@RequestParam(name = "id") int id) {
    final User user = findLoggedInUser();

    final Optional<Log> logOptional = logRepository.findById(id);
    if (!logOptional.isPresent() || logOptional.get().getUser().equals(user))
      throw new RestException(HttpStatus.NOT_FOUND, "Log with id=" + id + " not found");

    logRepository.delete(logOptional.get());
  }

  private User findLoggedInUser() {
    final String email = securityService.findLoggedInEmail();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found"));
  }
}
