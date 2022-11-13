package org.sample;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Set;

@Path("/extension/-NGkFt3H4vUX9E3gjkzr.json")
@RegisterRestClient
public interface ExtensionService {

    @GET
    Extension getById(@QueryParam("id") String id);
}
