package ru.vkulakov.phonex.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.vkulakov.phonex.Main;

import static org.junit.Assert.assertEquals;

public class SearchResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        Main.startServer();

        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        Main.shutdownServer();
    }

    @Test
    public void testSearch() {
        String expected = "Phone found";
        String actual = target.path("search").request().get(String.class);
        assertEquals("Полученный ответ не соответствует ожидаемому", expected, actual);
    }
}
