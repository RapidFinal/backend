package io.muic.rapid.jobme.api.user.security;

import io.muic.rapid.jobme.api.user.security.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryHandler authEntry;

    @Autowired
    private AuthenticationSuccessHandler authSuccess;

    @Autowired
    private AuthenticationFailureHandler authFailure;

    @Autowired
    private AuthenticationHandler authHandler;

//    @Autowired
//    private FacebookConnectionSignup facebookConnectionSignup;

    @Bean
    public JsonAuthenticationFilter authenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setContinueChainBeforeSuccessfulAuthentication(true);
        filter.setAuthenticationSuccessHandler(authSuccess);
        filter.setAuthenticationFailureHandler(authFailure);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authHandler);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] permitURI = new String[]{
                "/login",
                "/logout",
                "/user/register"
        };

        http
                .authorizeRequests()
                .antMatchers(permitURI).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginProcessingUrl("/login").permitAll()
                .successHandler(authSuccess).failureHandler(authFailure).permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntry)
                .and()
                .rememberMe().rememberMeCookieName("AUTOLOGIN")
                .and()
                .cors()
                .and()
                .logout().logoutUrl("/logout").permitAll()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "AUTOLOGIN")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll();

        http.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
