package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "POLICY")
public class Policy {

  @Id
  @Column(name = "POLICY_ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Column(name = "LAST_MODIFIED", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModified;

  @Column(name = "SITES", nullable = false)
  @NotNull
  private String sites;

  @Column(name = "APPLICATIONS", nullable = false)
  @NotNull
  private String applications;

  @Column(name = "SCHEDULE", nullable = false)
  @NotNull
  private String schedule;

  public Policy() {
    lastModified = Calendar.getInstance().getTime();
    sites = "{}";
    applications = "{}";
    schedule = "{}";
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

  @NotNull
  public Date getLastModified() {
    return lastModified;
  }

  @NotNull
  public String getSites() {
    return sites;
  }

  public void setSites(@NotNull String sites) {
    this.sites = sites;
  }

  @NotNull
  public String getApplications() {
    return applications;
  }

  public void setApplications(@NotNull String applications) {
    this.applications = applications;
  }

  @NotNull
  public String getSchedule() {
    return schedule;
  }

  public void setSchedule(@NotNull String schedule) {
    this.schedule = schedule;
  }
}
