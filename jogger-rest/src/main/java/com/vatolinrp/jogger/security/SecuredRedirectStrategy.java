package com.vatolinrp.jogger.security;

import com.vatolinrp.jogger.storage.SimpleDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class SecuredRedirectStrategy implements RedirectStrategy {

  @Autowired
  private SimpleDataBase simpleDataBase;

  @Override
  public void sendRedirect( HttpServletRequest request, HttpServletResponse response, String url )
    throws IOException {
    final String uuid = UUID.randomUUID().toString();
    simpleDataBase.getAuthenticationTokens().add( uuid );
    response.sendRedirect( response.encodeRedirectURL( "/secured" + request.getRequestURI() + "?authToken=" + uuid ) );
  }
}
