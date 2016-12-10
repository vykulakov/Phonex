package ru.vkulakov.phonex;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    private static Object lock = new Object();

    private static HttpServer server;

    // Base URI the Grizzly HTTP server will listen on
    public final static String BASE_URI = "http://localhost:8080/phonex/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     */
    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("ru.vkulakov.phonex.resources");

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Shutdowns Grizzly HTTP server.
     */
    public static void shutdownServer() {
        server.shutdownNow();
    }

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        startServer();

        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdownServer();
                lock.notifyAll();
            }
        });

        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                System.err.println("");
                e.printStackTrace(System.err);
            }
        }
    }
}

