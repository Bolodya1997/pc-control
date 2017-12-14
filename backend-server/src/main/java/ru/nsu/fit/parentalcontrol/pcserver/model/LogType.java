package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOG_TYPE")
public class LogType {

  @Id
  @Column(name = "TYPE_ID")
  private Integer id;

  @Column(name = "TYPE_NAME", nullable = false, unique = true)
  private String name;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
