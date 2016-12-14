package ru.vkulakov.phonex;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.utils.Setup;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h3>Основной класс для запуска и инициализации приложения</h3>
 */
public class Main {
    private static HttpServer server;

	private final static Object lock = new Object();

	/**
	 * <p>Инициализация приложения.</p>
	 */
	private static void init() {
		try (
			Connection conn = Setup.getConnection();
		) {
			Setup.initDatabase(conn);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка закрытия подключения к базе данных", e);
		}
	}

    /**
     * <p>Запуск Grizzly HTTP сервера для предоставления JAX-RS ресурсов, описанных в приложении.</p>
     */
    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("ru.vkulakov.phonex.resources");

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Setup.makeBaseUri()), rc);
    }

    /**
     * <p>Остановка Grizzly HTTP сервера.</p>
     */
    public static void shutdownServer() {
        server.shutdownNow();
    }

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
    	init();
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
