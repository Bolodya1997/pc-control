package ru.nsu.fit.parentalcontrol.pcserver.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CHILD")
public class Child {

  @Id
  @Column(name = "CHILD_ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Column(name = "NAME", nullable = false)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "POLICY_ID", nullable = false)
  private Policy policy;

  public Child() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Policy getPolicy() {
    return policy;
  }

  public void setPolicy(@NotNull Policy policy) {
    this.policy = policy;
  }
}
