package com.vatolinrp.jogger.rest.service;

import com.vatolinrp.jogger.model.Run;
import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Service
@Produces( MediaType.APPLICATION_JSON )
@Path( "/" )
public class JoggerService {

  private static final Logger logger = LoggerFactory.getLogger( JoggerService.class );

  @Autowired
  private SimpleDataBase simpleDataBase;

  @GET
  @Path("/ping")
  public String ping() {

    return "ping";
  }

  @GET
  @Path("/users")
  public List<User> getAllUsers()
  {
    return simpleDataBase.getUsers();
  }

  @POST
  @Path("/users/{login}/runs")
  public Response addRun( @PathParam("login") final String login,
    @HeaderParam("password") final String password, final Run run )
  {
    final User jogger = simpleDataBase.getUsers().stream()
      .filter( user -> login.equals( user.getLogin() ) && password.equals( user.getPassword() ) )
      .findFirst().get();
    jogger.getJogHistory().put( LocalDate.now().toString(), run );
    return Response.ok().build();
  }
}
