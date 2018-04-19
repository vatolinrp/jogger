package com.vatolinrp.jogger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User
{
  private String name;

  private String login;

  @JsonIgnore
  private String password;

  private Map<LocalDate, Run> jogHistory;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Map<LocalDate, Run> getJogHistory() {
    if ( jogHistory == null ) {
      jogHistory = new HashMap<>();
    }
    return jogHistory;
  }

  public void setJogHistory(Map<LocalDate, Run> jogHistory) {
    this.jogHistory = jogHistory;
  }
}
