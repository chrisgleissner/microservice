package com.github.chrisgleissner.microservice.wildfly.ping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ping")
public class PingResource {

	@Inject
	PingService pingService;

	@GET
	@Produces({"application/json"})
	public String ping() {
		return "{\"pong\":\"" + pingService.currentTimeMillis() + "\"}";
	}
}