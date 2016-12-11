package ru.vkulakov.phonex;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public final static String BASE_URI = "http://localhost:8080/phonex/";

    private static HttpServer server;

	private final static Object lock = new Object();

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

		System.out.println(String.format("Рабочая директория: %s", System.getProperty("user.dir")));
		System.out.println(String.format("Текущий профиль: %s", PhonexProperties.getInstance().getProperty("profile")));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Получен сигнал завершения приложения");

			shutdownServer();
			synchronized (lock) {
				lock.notifyAll();
			}
		}));

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                System.err.println("Ошибка ожидания завершения приложения");
                e.printStackTrace(System.err);
            }
        }
    }
}
