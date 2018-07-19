package com.vatolinrp.jogger.rest.service;

import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service
@Path( "/" )
@Produces( MediaType.TEXT_PLAIN )
public class PublicJoggerService {

  @Autowired
  private UserAuthenticationService userAuthenticationService;

  @GET
  @Path("/ping")
  public String ping() {
    return "pong";
  }

  @POST
  @Path("/login")
  @Consumes( MediaType.APPLICATION_JSON )
  public String login( final User user ) {
    return userAuthenticationService.login( user.getUsername(), user.getPassword() )
      .orElseThrow( () -> new RuntimeException( "invalid login and/or password" ) );
  }
}
