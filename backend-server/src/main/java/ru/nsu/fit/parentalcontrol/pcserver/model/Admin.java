package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ADMIN")
public class Admin {

  @Id
  @Column(name = "USER_ID", nullable = false)
  private Integer userId;

  @OneToOne(optional = false)
  @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
  private User user;

  public Admin() {
  }

  public Admin(@NotNull User user) {
    setUser(user);
  }

  public User getUser() {
    return user;
  }

  public void setUser(@NotNull User user) {
    this.user = user;
    userId = user.getId();
  }
}
