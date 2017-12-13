package ru.nsu.fit.parentalcontrol.pcserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "SUBSCRIPTION_ID")
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "TYPE_ID", nullable = false)
  private SubscriptionType type;

  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "STARTS", nullable = false)
  private Date starts;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRES", nullable = false)
  private Date expires;

  public Subscription() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public SubscriptionType getType() {
    return type;
  }

  public void setType(@NotNull SubscriptionType type) {
    this.type = type;
  }

  public User getUser() {
    return user;
  }

  public void setUser(@NotNull User user) {
    this.user = user;
  }

  public Date getStarts() {
    return starts;
  }

  public void setStarts(@NotNull Date starts) {
    this.starts = starts;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(@NotNull Date expires) {
    this.expires = expires;
  }
}
