package com.vatolinrp.jogger.rest.service;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan( value = "com.vatolinrp.jogger" )
public class RestService {

  private static final String basePath = "/*";

  @Autowired
  private Bus bus;

  @Autowired
  private SecuredJoggerService securedJoggerService;

  @Autowired
  private PublicJoggerService publicJoggerService;

  public static void main( String[] args ) {
    final int port = Integer.valueOf( System.getenv( "PORT" ) );
    System.getProperties().put( "server.port", port );
    SpringApplication.run( RestService.class, args );
  }

  @Bean
  public Server rsServer() {
    final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
    endpoint.setBus( bus );
    endpoint.setServiceBeans( Arrays.asList( securedJoggerService, publicJoggerService ) );
    endpoint.setProvider( new JacksonJsonProvider() );
    return endpoint.create();
  }

  @Bean
  public ServletRegistrationBean cxfServletRegistrationBean() {
    return new ServletRegistrationBean( new CXFServlet(), basePath );
  }
}
