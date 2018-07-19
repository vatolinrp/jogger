package com.vatolinrp.jogger.model;

public class Run {
  private Integer id;
  private String date;
  private String timeSpent;
  private Double distance;
  private Double speed;

  public String getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(String timeSpent) {
    this.timeSpent = timeSpent;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public Double getSpeed() {
    return speed;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
