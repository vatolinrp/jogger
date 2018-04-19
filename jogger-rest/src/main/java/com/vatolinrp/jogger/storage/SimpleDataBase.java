package com.vatolinrp.jogger.storage;

import com.vatolinrp.jogger.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class SimpleDataBase
{
  private List<User> users;

  public SimpleDataBase() {
    this.users = new ArrayList<>();
    final User admin = new User();
    admin.setLogin( "admin" );
    admin.setName( "Admin's name" );
    admin.setPassword("admin");
    final User supporter = new User();
    supporter.setLogin("supporter");
    supporter.setPassword("supporter");
    supporter.setName("Supporter's name");
    this.users.add( admin );
    this.users.add( supporter );
  }

  public List<User> getUsers() {
    return users;
  }
}
