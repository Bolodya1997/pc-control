package ru.nsu.fit.parentalcontrol.pcserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.model.Log;

public interface LogRepository extends CrudRepository<Log, Integer> {

}
