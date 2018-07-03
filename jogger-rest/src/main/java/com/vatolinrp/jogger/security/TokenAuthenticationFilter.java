package com.vatolinrp.jogger.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  private static final String BEARER = "Bearer";

  public TokenAuthenticationFilter( final RequestMatcher requiresAuth ) {
    super( requiresAuth );
  }

  @Override
  public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
    final String param = Optional.ofNullable( request.getHeader( HttpHeaders.AUTHORIZATION ) )
      .orElse( request.getParameter( "t" ) );
    final String token = Optional.ofNullable(param)
      .map( value -> value.replace( BEARER, "" ) )
      .map( String::trim )
      .orElseThrow( () -> new BadCredentialsException( "Missing Authentication Token" ) );
    final Authentication auth = new UsernamePasswordAuthenticationToken( token, token );
    return getAuthenticationManager().authenticate( auth );
  }
}
