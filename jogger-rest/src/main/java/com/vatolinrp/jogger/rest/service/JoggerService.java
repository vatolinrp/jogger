package com.vatolinrp.jogger.rest.service;

import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service
@Produces( MediaType.APPLICATION_JSON )
@Path( "/" )
public class JoggerService {

  @GET
  @Path("/ping")
  public String ping() {

    return "ping";
  }
}
