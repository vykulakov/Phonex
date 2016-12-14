package ru.vkulakov.phonex.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("search/{phone}")
public class SearchResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("phone") String phoneStr) {
        return "Phone found";
    }
}
