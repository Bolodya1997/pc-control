package ru.nsu.fit.parentalcontrol.pcserver.security.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, String> {

}
