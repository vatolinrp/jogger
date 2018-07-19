package com.vatolinrp.jogger.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled=true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private TokenAuthenticationProvider provider;
  private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher( new AntPathRequestMatcher( "/users/**" ) );
  private static final RequestMatcher PUBLIC_URLS = new NegatedRequestMatcher( PROTECTED_URLS );

  public SecurityConfig( final TokenAuthenticationProvider provider ) {
    super();
    this.provider = provider;
  }

  @Autowired
  private SecuredRedirectStrategy securedRedirectStrategy;

  @Override
  protected void configure( final AuthenticationManagerBuilder auth ) {
    auth.authenticationProvider( provider );
  }

  @Override
  public void configure( final WebSecurity web ) {
    web.ignoring().requestMatchers( PUBLIC_URLS );
  }

  @Override
  protected void configure( final HttpSecurity http )
    throws Exception {
    http
      .sessionManagement()
      .sessionCreationPolicy( STATELESS )
      .and()
      .exceptionHandling()
      .defaultAuthenticationEntryPointFor( forbiddenEntryPoint(), PROTECTED_URLS )
      .and()
      .authenticationProvider( provider )
      .addFilterBefore( restAuthenticationFilter(), AnonymousAuthenticationFilter.class )
      .authorizeRequests()
      .anyRequest()
      .authenticated()
      .and()
      .csrf().disable()
      .formLogin().disable()
      .httpBasic().disable()
      .logout().disable();
  }

  @Bean
  public TokenAuthenticationFilter restAuthenticationFilter()
    throws Exception {
    final TokenAuthenticationFilter filter = new TokenAuthenticationFilter( PROTECTED_URLS );
    filter.setAuthenticationManager( authenticationManager() );
    filter.setAuthenticationSuccessHandler( successHandler() );
    return filter;
  }

  @Bean
  public SimpleUrlAuthenticationSuccessHandler successHandler() {
    final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setRedirectStrategy( securedRedirectStrategy );
    return successHandler;
  }

  /**
   * Disable Spring boot automatic filter registration.
   */
  @Bean
  public FilterRegistrationBean disableAutoRegistration( final TokenAuthenticationFilter filter ) {
    final FilterRegistrationBean registration = new FilterRegistrationBean( filter );
    registration.setEnabled( false );
    return registration;
  }

  @Bean
  public AuthenticationEntryPoint forbiddenEntryPoint() {
    return new HttpStatusEntryPoint( FORBIDDEN );
  }
}
