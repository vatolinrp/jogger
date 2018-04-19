package com.vatolinrp.jogger.rest.service;

import com.vatolinrp.jogger.model.User;
import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service
@Produces( MediaType.APPLICATION_JSON )
@Path( "/" )
public class JoggerService {

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
}
