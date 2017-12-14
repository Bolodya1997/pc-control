package ru.nsu.fit.parentalcontrol.pcserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID", nullable = false)
  private Integer id;

  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @OneToMany(mappedBy = "user")
  private Set<Subscription> subscriptions;

  @OneToMany(mappedBy = "user")
  private Set<Policy> policies;

  @OneToMany(mappedBy = "user")
  private Set<Child> children;

  public User() {
  }

  public User(@NotNull String email, @NotNull String password) {
    this.email = email;
    this.password = password;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(@NotNull String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(@NotNull String password) {
    this.password = password;
  }

  public Set<Subscription> getSubscriptions() {
    return subscriptions;
  }

  @JsonIgnore
  public Subscription getActualSubscription() {
    final Date now = Calendar.getInstance().getTime();

    return subscriptions.stream()
        .filter(subscription -> subscription.getStarts().before(now))
        .filter(subscription -> subscription.getExpires().after(now))
        .findAny().orElse(null);
  }

  public Set<Policy> getPolicies() {
    return policies;
  }

  public Set<Child> getChildren() {
    return children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    final User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
