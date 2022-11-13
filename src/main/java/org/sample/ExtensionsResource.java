package org.sample;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Set;

@Path("/extensions")
public class ExtensionsResource {

    @Inject
    @RestClient
    ExtensionService extensionService;

    @GET
    @Path("/id/{id}")
    public Extension id(@PathParam("id") String id) {
        return extensionService.getById(id);
    }
}
