package ru.nsu.fit.parentalcontrol.pcserver.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AUTH")
public class Auth {

  @Id
  @Column(name = "EMAIL")
  @NotNull
  private String email;

  @Column(name = "PASSWORD")
  @NotNull
  private String password;

  public Auth() {
  }

  public Auth(@NotNull String email, @NotNull String password) {
    this.email = email;
    this.password = password;
  }

  @NotNull
  public String getEmail() {
    return email;
  }

  public void setEmail(@NotNull String email) {
    this.email = email;
  }

  @NotNull
  public String getPassword() {
    return password;
  }

  public void setPassword(@NotNull String password) {
    this.password = password;
  }
}
