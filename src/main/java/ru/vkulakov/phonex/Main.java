package ru.vkulakov.phonex;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.utils.Setup;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * Основной класс для запуска и инициализации приложения.
 */
public class Main {
    private static HttpServer server;
	private static ScheduledExecutorService executorService;

	private final static Object lock = new Object();

	/**
	 * <p>Инициализация приложения.</p>
	 */
	public static void init() {
		System.out.println("Инициализация приложения");

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
		System.out.println("Запуск Grizzly HTTP сервера");

        ResourceConfig resourceConfig = new ResourceConfig().packages("ru.vkulakov.phonex.resources");

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Setup.makeBaseUri()), resourceConfig);
    }

    /**
     * <p>Остановка Grizzly HTTP сервера.</p>
     */
    public static void shutdownServer() {
		System.out.println("Остановка Grizzly HTTP сервера");

        server.shutdownNow();
    }

	/**
	 * Запуск планировщика для переодического обновления базы номером телефонов.
	 */
	private static void startLoader() {
		System.out.println("Запуск загрузчика");

		executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(new Loader(), 10, 60, TimeUnit.SECONDS);
	}

	/**
	 * Остановка планировщика.
	 */
	private static void shutdownLoader() {
		System.out.println("Остановка загрузчика");

		try {
			// Отменяем запущенные операции.
			executorService.shutdownNow();

			// Ждём завершения запущенных задач.
			if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				System.err.println("Истекло время ожидания остановки загрузчика");
			}
		} catch (InterruptedException e) {
			System.err.println("Ошибка ожидания остановки загрузчика");
			e.printStackTrace(System.err);
		}

		System.out.println("Окончание остановки загрузчика");
	}

	/**
     * Главный метод для запуска приложения.
     * @param args
     */
    public static void main(String[] args) {
    	init();
        startServer();
        startLoader();

		System.out.println(String.format("Рабочая директория: %s", System.getProperty("user.dir")));
		System.out.println(String.format("Текущий профиль: %s", PhonexProperties.getInstance().getProperty("profile")));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Получен сигнал завершения приложения");

			shutdownLoader();
			shutdownServer();

			synchronized (lock) {
				lock.notifyAll();

				try {
					System.out.println("Ожидание перед завершением приложения");

					Thread.sleep(5000);

					System.out.println("Конец ожидания перед завершением приложения");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}));

        synchronized (lock) {
            try {
                lock.wait();

				System.out.println("Завершение ожидания");
            } catch (InterruptedException e) {
                System.err.println("Ошибка ожидания завершения приложения");
                e.printStackTrace(System.err);
            }
        }

		System.out.println("Остановка приложения");
    }
}
