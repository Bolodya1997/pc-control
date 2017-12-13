package ru.nsu.fit.parentalcontrol.pcserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.StreamSupport;

public interface UserRepository extends CrudRepository<User, Integer> {

  default Optional<User> findByEmail(@NotNull String email) {
    return StreamSupport.stream(findAll().spliterator(), true)
        .filter(user -> email.equals(user.getEmail()))
        .findAny();
  }
}
