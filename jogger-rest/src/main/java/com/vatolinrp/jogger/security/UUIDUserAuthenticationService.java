package com.vatolinrp.jogger.security;

import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UUIDUserAuthenticationService implements UserAuthenticationService {

  @Autowired
  private SimpleDataBase simpleDataBase;

  @Override
  public Optional<String> login( String username, String password ) {
    final String uuid = UUID.randomUUID().toString();
    final User user = simpleDataBase.getUsers().get( username );
    if ( user != null && password.equals( user.getPassword() ) ) {
      user.setToken( uuid );
    }
    return Optional.of(uuid);
  }

  @Override
  public Optional<User> findByToken( String token ) {
    return simpleDataBase.getUsers().values().stream().filter( user -> token.equals( user.getToken() ) ).findFirst();
  }

  @Override
  public void logout(User user) {

  }
}
