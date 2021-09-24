package org.acme.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.domain.FruitData;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

@RegisterRestClient(configKey = "fruity-vice")
@Path("/api/fruit")
public interface FruityViceClient {
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	Uni<FruitData> getByName(@PathParam("name") String name);
}
