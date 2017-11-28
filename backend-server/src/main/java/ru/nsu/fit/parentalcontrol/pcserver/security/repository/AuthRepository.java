package ru.nsu.fit.parentalcontrol.pcserver.security.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;

public interface AuthRepository extends CrudRepository<Auth, String> {

}
