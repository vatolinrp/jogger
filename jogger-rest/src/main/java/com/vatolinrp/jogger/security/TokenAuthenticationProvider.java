package com.vatolinrp.jogger.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private UserAuthenticationService userAuthenticationService;

  @Override
  protected void additionalAuthenticationChecks( UserDetails userDetails,
    UsernamePasswordAuthenticationToken authentication)
  throws AuthenticationException {}

  @Override
  protected UserDetails retrieveUser( String username, UsernamePasswordAuthenticationToken authentication )
  throws AuthenticationException {
    final Object token = authentication.getCredentials();
    return Optional
      .ofNullable( token )
      .map( String::valueOf )
      .flatMap( userAuthenticationService::findByToken )
      .orElseThrow( () -> new UsernameNotFoundException( "Cannot find user with authentication token=" + token ) );
  }
}
