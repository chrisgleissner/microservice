package com.github.chrisgleissner.microservice.springboot.rest.security;

import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create.JwtUsernameAndPasswordAuthenticationFilter;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create.UserConstants;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.verify.JwtTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity @RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(SC_UNAUTHORIZED))

                .and().addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig)) // for issue side
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(POST, jwtConfig.getUri()).permitAll() // allow all who are accessing "/login"
                .antMatchers("/api/admin/**").hasRole(UserConstants.ADMIN_ROLE) // must be a authenticated and an admin for '/api/admin'
                .anyRequest().authenticated(); // Any other request must be authenticated
    }

    @Override
    public void configure(WebSecurity web) {
        // Open up Swagger
        web.ignoring().antMatchers(
                "/api/ping",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}