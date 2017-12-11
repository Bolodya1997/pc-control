package ru.nsu.fit.parentalcontrol.pcserver.security.repository;

import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AuthRepositoryMock implements AuthRepository {

  private final Map<String, Auth> entries = new HashMap<>();

  AuthRepositoryMock() {
    entries.put(MockUsers.USER_EMAIL, new Auth(MockUsers.USER_EMAIL, MockUsers.PASSWORD));
    entries.put(MockUsers.ADMIN_EMAIL, new Auth(MockUsers.ADMIN_EMAIL, MockUsers.PASSWORD));
  }

  @Override
  public <S extends Auth> S save(S s) {
    return null;
  }

  @Override
  public <S extends Auth> Iterable<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  @Override
  public Optional<Auth> findById(String s) {
    final Auth auth = entries.get(s);
    return (auth == null) ? Optional.empty()
                          : Optional.of(entries.get(s));
  }

  @Override
  public boolean existsById(String s) {
    return entries.containsKey(s);
  }

  @Override
  public Iterable<Auth> findAll() {
    return entries.values();
  }

  @Override
  public Iterable<Auth> findAllById(Iterable<String> iterable) {
    final Iterator<String> i = iterable.iterator();
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
  public void delete(Auth auth) {
  }

  @Override
  public void deleteAll(Iterable<? extends Auth> iterable) {
  }

  @Override
  public void deleteAll() {
  }
}
