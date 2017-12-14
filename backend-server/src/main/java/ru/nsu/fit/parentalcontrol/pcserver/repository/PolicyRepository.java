package ru.nsu.fit.parentalcontrol.pcserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.model.Policy;

public interface PolicyRepository extends CrudRepository<Policy, Integer> {

}
