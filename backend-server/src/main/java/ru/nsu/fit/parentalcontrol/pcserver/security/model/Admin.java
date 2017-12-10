package ru.nsu.fit.parentalcontrol.pcserver.security.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ADMIN")
public class Admin {

  @Id
  @Column(name = "EMAIL")
  @NotNull
  private String email;

  public Admin() {
  }

  public Admin(@NotNull String email) {
    this.email = email;
  }

  @NotNull
  public String getEmail() {
    return email;
  }

  public void setEmail(@NotNull String email) {
    this.email = email;
  }
}
