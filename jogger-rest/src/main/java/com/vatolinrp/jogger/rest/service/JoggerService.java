package com.vatolinrp.jogger.rest.service;

import com.vatolinrp.jogger.model.Run;
import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

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
  public Response getAllUsers()
  {
    return Response.status( HttpStatus.OK.value() ).entity( simpleDataBase.getUsers() ).build();
  }

  @POST
  @Path("/users")
  public Response createUser( final User user )
  {
    simpleDataBase.getUsers().add( user );
    return Response.status( HttpStatus.CREATED.value() ).build();
  }

  @PUT
  @Path("/users/{login}")
  public Response updateUser( @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    final User userToUpdate, @PathParam("login") final String joggersLogin )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final User outdatedUser = simpleDataBase.getUsers().stream()
        .filter( user -> user.getLogin().equals( joggersLogin ) )
        .findFirst().get();
      simpleDataBase.getUsers().remove( outdatedUser );
      simpleDataBase.getUsers().add( userToUpdate );
      return Response.status( HttpStatus.NO_CONTENT.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @DELETE
  @Path("/users/{login}")
  public Response deleteUser( @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    @PathParam("login") final String joggersLogin )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final User outdatedUser = simpleDataBase.getUsers().stream()
        .filter( user -> user.getLogin().equals( joggersLogin ) )
        .findFirst().get();
      simpleDataBase.getUsers().remove( outdatedUser );
      return Response.status( HttpStatus.NO_CONTENT.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @POST
  @Path("/users/{login}/runs")
  public Response addRun( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    final Run run )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      run.setId( ThreadLocalRandom.current().nextInt(0, 999) );
      simpleDataBase.getUsers().stream().filter( user -> user.getLogin().equals( joggersLogin ) ).findFirst()
        .ifPresent( user -> user.getJogHistory().put( run.getId(), run ) );
      return Response.status( HttpStatus.CREATED.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @GET
  @Path("/users/{login}/runs")
  public Response getRuns( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final Collection<Run> runs = simpleDataBase.getUsers().stream()
        .filter( user -> user.getLogin().equals( joggersLogin ) ).findFirst()
        .get().getJogHistory().values();
      return Response.status( HttpStatus.OK.value() ).entity( runs ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @PUT
  @Path("/users/{login}/runs/{runId}")
  public Response updateRun( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    @PathParam("runId") final String runId, final Run run  )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final User jogger = simpleDataBase.getUsers().stream()
        .filter( user -> user.getLogin().equals( joggersLogin ) ).findFirst().get();
      jogger.getJogHistory().remove( Integer.valueOf( runId ) );
      jogger.getJogHistory().put( Integer.valueOf( runId ), run );
      return Response.status( HttpStatus.OK.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @DELETE
  @Path("/users/{login}/runs/{runId}")
  public Response deleteRun( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    @PathParam("runId") final String runId  )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      simpleDataBase.getUsers().stream()
        .filter( user -> user.getLogin().equals( joggersLogin ) ).findFirst()
        .ifPresent( user -> user.getJogHistory().remove( Integer.valueOf( runId ) ) );
      return Response.status( HttpStatus.OK.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  private boolean isAuthorized( final String login, final String password )
  {
    return simpleDataBase.getUsers().stream()
      .anyMatch(user -> login.equals( user.getLogin() ) && password.equals( user.getPassword() ) );
  }
}
