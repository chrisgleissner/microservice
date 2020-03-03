package com.github.chrisgleissner.microservice.quarkus.securedping;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/securedPing") @RequiredArgsConstructor
public class SecuredPingResource {
    private final JsonWebToken jwt;

    @GET @RolesAllowed({"Admin"}) @Produces(APPLICATION_JSON)
    public String ping(@Context SecurityContext ctx) {
        return String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s",
                Optional.ofNullable(ctx.getUserPrincipal()).map(Principal::getName).orElse("anonymous"),
                ctx.isSecure(), ctx.getAuthenticationScheme(), jwt.getClaimNames() != null);
    }
}