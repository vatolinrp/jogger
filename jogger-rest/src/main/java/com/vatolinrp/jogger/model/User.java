package com.vatolinrp.jogger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class User
{
  private String name;

  private String login;

  @JsonIgnore
  private String password;

  private Map<Integer, Run> jogHistory;

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

  public Map<Integer, Run> getJogHistory() {
    if ( jogHistory == null ) {
      jogHistory = new HashMap<>();
    }
    return jogHistory;
  }

  public void setJogHistory(Map<Integer, Run> jogHistory) {
    this.jogHistory = jogHistory;
  }
}
