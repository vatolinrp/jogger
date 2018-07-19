package com.vatolinrp.jogger.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class User implements UserDetails {
  private String name;

  private String login;

  private String password;

  private String token;

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
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

  public String getToken() {
    return token;
  }

  public void setToken( String token ) {
    this.token = token;
  }
}
