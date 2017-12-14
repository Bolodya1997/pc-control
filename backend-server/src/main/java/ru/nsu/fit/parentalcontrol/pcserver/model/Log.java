package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "LOG")
public class Log {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LOG_ID")
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumns({
      @JoinColumn(name = "USER_ID", nullable = false),
      @JoinColumn(name = "CHILD_ID", nullable = false)
  })
  private Child child;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "TIME", nullable = false)
  private Date time;

  @Column(name = "INFO", nullable = false)
  private String info;

  @ManyToOne(optional = false)
  @JoinColumn(name = "LOG_ID", nullable = false)
  private LogType type;

  public Integer getId() {
    return id;
  }

  public Child getChild() {
    return child;
  }

  public void setChild(@NotNull Child child) {
    this.child = child;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(@NotNull Date time) {
    this.time = time;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(@NotNull String info) {
    this.info = info;
  }

  public LogType getType() {
    return type;
  }

  public void setType(@NotNull LogType type) {
    this.type = type;
  }
}
