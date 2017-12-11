package ru.nsu.fit.parentalcontrol.pcserver.security.repository;

import ru.nsu.fit.parentalcontrol.pcserver.security.model.Admin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AdminRepositoryMock implements AdminRepository {

  private final Map<String, Admin> entries = new HashMap<>();

  AdminRepositoryMock() {
    entries.put(MockUsers.ADMIN_EMAIL, new Admin(MockUsers.ADMIN_EMAIL));
  }

  @Override
  public <S extends Admin> S save(S entity) {
    return null;
  }

  @Override
  public <S extends Admin> Iterable<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Admin> findById(String s) {
    final Admin admin = entries.get(s);
    return (admin == null) ? Optional.empty()
                           : Optional.of(admin);
  }

  @Override
  public boolean existsById(String s) {
    return entries.containsKey(s);
  }

  @Override
  public Iterable<Admin> findAll() {
    return entries.values();
  }

  @Override
  public Iterable<Admin> findAllById(Iterable<String> strings) {
    final Iterator<String> i = strings.iterator();
    return Stream.generate(() -> entries.get(i.next()))
        .takeWhile(s -> i.hasNext())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public long count() {
    return entries.size();
  }

  @Override
  public void deleteById(String s) {
  }

  @Override
  public void delete(Admin entity) {
  }

  @Override
  public void deleteAll(Iterable<? extends Admin> entities) {
  }

  @Override
  public void deleteAll() {
  }
}
