package com.vatolinrp.jogger.rest.service;

import com.vatolinrp.jogger.model.Run;
import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@Path( "/" )
public class SecuredJoggerService {

  private static final Logger logger = LoggerFactory.getLogger( SecuredJoggerService.class );
  private static final String FILENAME_PARAM = "filename";

  @Autowired
  private SimpleDataBase simpleDataBase;

  @GET
  @Path("/users")
  public Response getAllUsers( @AuthenticationPrincipal final User user )
  {
    return Response.status( HttpStatus.OK.value() ).entity( simpleDataBase.getUsers() ).build();
  }

  @GET
  @Path("/users/{login}")
  public Response getUser( @PathParam("login") final String joggersLogin )
  {
    return Response.status( HttpStatus.OK.value() ).entity( simpleDataBase.getUsers().get( joggersLogin ) ).build();
  }

  @POST
  @Path("/users")
  @Consumes( MediaType.MULTIPART_FORM_DATA )
  public Response createUser( @Multipart("upfile") final Attachment attachment, @Multipart("name") final String name,
    @Multipart("login") final String login, @Multipart("password") final String password )
  {
    final User user = new User();
    user.setName( name );
    user.setLogin( login );
    user.setPassword( password );
    simpleDataBase.getUsers().put( user.getLogin(), user );
    final String filename = attachment.getContentDisposition().getParameter( FILENAME_PARAM );
    java.nio.file.Path path = Paths.get( filename );
    deleteIfExists( path );
    copy( attachment.getObject( InputStream.class ), path );
    return Response.status( HttpStatus.CREATED.value() ).build();
  }

  private void deleteIfExists( final java.nio.file.Path path ) {
    try {
      Files.deleteIfExists( path );
    } catch ( final IOException e ) {
      logger.error( "Tried to delete existing file. Exception occurred: {}", e );
    }
  }

  private void copy( final InputStream inputStream, final java.nio.file.Path path ) {
    try {
      Files.copy( inputStream, path );
    } catch ( final IOException e ) {
      logger.error( "Tried save file. Exception occurred: {}", e );
    }
  }

  @PUT
  @Path("/users/{login}")
  public Response updateUser( @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    final User userToUpdate, @PathParam("login") final String joggersLogin )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      simpleDataBase.getUsers().remove( joggersLogin );
      simpleDataBase.getUsers().put( userToUpdate.getLogin(), userToUpdate );
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
      simpleDataBase.getUsers().remove( joggersLogin );
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
      simpleDataBase.getUsers().get( joggersLogin ).getJogHistory().put( run.getId(), run );
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
      final Collection<Run> runs = simpleDataBase.getUsers().get( joggersLogin ).getJogHistory().values();
      return Response.status( HttpStatus.OK.value() ).entity( runs ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @GET
  @Path("/users/{login}/runs/{runId}")
  public Response getRun( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    @PathParam("runId") final String runId )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final Run run = simpleDataBase.getUsers().get( joggersLogin ).getJogHistory().get(Integer.valueOf( runId ) );
      return Response.status( HttpStatus.OK.value() ).entity( run ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  @GET
  @Path("/users/{login}/reports/runs")
  public Response getReport( @PathParam("login") final String joggersLogin,
    @HeaderParam("password") final String password, @HeaderParam("login") final String login,
    @PathParam("runId") final String runId )
  {
    boolean isAuthorized = isAuthorized( login, password );
    if ( isAuthorized ) {
      final Map<Integer,Run> jogHistory = simpleDataBase.getUsers().get( joggersLogin ).getJogHistory();
      Map<LocalDate,Run> joggings = new HashMap<>();
      for( Run run: jogHistory.values() ) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        joggings.put( LocalDate.parse(run.getDate(), formatter), run);
      }
      SortedSet<LocalDate> keys = new TreeSet<>(joggings.keySet());
      List<Run> sortedRuns = new ArrayList<>();
      for (LocalDate key : keys) {
        sortedRuns.add( joggings.get(key) );
      }
      return Response.status( HttpStatus.OK.value() ).entity( sortedRuns ).build();
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
      final User jogger = simpleDataBase.getUsers().get( joggersLogin );
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
      simpleDataBase.getUsers().get( joggersLogin ).getJogHistory().remove( Integer.valueOf( runId ) );
      return Response.status( HttpStatus.OK.value() ).build();
    }
    return Response.status( HttpStatus.UNAUTHORIZED.value() ).build();
  }

  private boolean isAuthorized( final String login, final String password )
  {
    return simpleDataBase.getUsers().get( login ).getPassword().equals( password );
  }
}
