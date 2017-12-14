package ru.nsu.fit.parentalcontrol.pcserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.model.LogType;

import java.util.Optional;
import java.util.stream.StreamSupport;

public interface LogTypeRepository extends CrudRepository<LogType, Integer> {

  default Optional<LogType> findByName(String name) {
    return StreamSupport.stream(findAll().spliterator(), true)
        .filter(logType -> logType.getName().equals(name))
        .findAny();
  }
}
