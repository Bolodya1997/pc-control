package ru.nsu.fit.parentalcontrol.pcserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.model.Admin;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

  default Optional<Admin> findByUser(@NotNull User user) {
    return findById(user.getId());
  }
}
