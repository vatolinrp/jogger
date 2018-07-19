package com.vatolinrp.jogger.storage;

import com.vatolinrp.jogger.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class SimpleDataBase {

  private Map<String,User> users;

  public SimpleDataBase() {
    this.users = new HashMap<>();
    final User admin = new User();
    admin.setLogin( "admin" );
    admin.setName( "Admin's name" );
    admin.setPassword("admin");
    final User supporter = new User();
    supporter.setLogin("supporter");
    supporter.setPassword("supporter");
    supporter.setName("Supporter's name");
    this.users.put( admin.getLogin(), admin );
    this.users.put( supporter.getLogin(), supporter );
  }

  public Map<String,User> getUsers() {
    return users;
  }
}
