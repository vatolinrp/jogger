package com.vatolinrp.jogger.model;

import java.time.LocalTime;

public class Run
{
  private LocalTime timeSpent;
  private Double distance;
  private Double speed;

  public LocalTime getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(LocalTime timeSpent) {
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
}
