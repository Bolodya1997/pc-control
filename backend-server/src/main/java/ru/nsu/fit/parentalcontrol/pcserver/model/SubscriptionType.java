package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SUBSCRIPTION_TYPE")
public class SubscriptionType {

  @Id
  @Column(name = "TYPE_ID")
  private Integer id;

  @Column(name = "TYPE_NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  @NotNull
  private String description;

  public SubscriptionType() {
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @NotNull
  public String getDescription() {
    return description;
  }
}
