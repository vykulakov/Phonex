package ru.vkulakov.phonex.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("search")
public class SearchResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String search() {
        return "Phone found";
    }
}
