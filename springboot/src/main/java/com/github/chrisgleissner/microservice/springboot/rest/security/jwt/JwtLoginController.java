package com.github.chrisgleissner.microservice.springboot.rest.security.jwt;

import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtUsernameAndPasswordAuthenticationFilter.UserCredentials;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.LOGIN_PATH;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

// TODO Replace this with an appropriate Swagger config
/**
 * Shadowed by {@link JwtUsernameAndPasswordAuthenticationFilter} and only exists so a login endpoint appears in Swagger.
 */
@RestController @RequestMapping(value = LOGIN_PATH, produces = TEXT_PLAIN_VALUE)
public class JwtLoginController {

	@PostMapping
	public String login(@RequestBody UserCredentials userCredentials) {
		return null;
	}
}