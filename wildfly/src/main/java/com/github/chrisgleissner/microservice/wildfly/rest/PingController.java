package com.github.chrisgleissner.microservice.wildfly.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ping")
public class PingController {
	@Inject
	private PingService service;

	@GET
	@Produces({ "application/json" })
	public String ping() {
		return "{\"pong\":\"" + service.currentTimeMillis() + "\"}";
	}}