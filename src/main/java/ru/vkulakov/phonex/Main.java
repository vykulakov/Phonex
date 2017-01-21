/*
 * Phonex - https://github.com/vykulakov/Phonex
 *
 * Copyright 2016 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vkulakov.phonex;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vkulakov.phonex.utils.PhonexProperties;
import ru.vkulakov.phonex.utils.Setup;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Основной класс для запуска и инициализации приложения.
 */
public class Main {
	/**
	 * Grizzly HTTP сервер
	 */
    private static HttpServer server;

	/**
	 * Планировщик заданий для загрузки данных в базу.
	 */
	private static ScheduledExecutorService executorService;

	/**
	 * Объект для корректного завершения приложения
	 */
	private final static CountDownLatch latch = new CountDownLatch(1);

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Инициализация приложения.
	 * Создаёт базу данных и необходимые для работы приложения таблицы в базе.
	 */
	public static void init() {
		logger.debug("Инициализация приложения");

		try (
			Connection conn = Setup.getConnection()
		) {
			Setup.initDatabase(conn);
		} catch (SQLException e) {
			logger.warn("Ошибка закрытия подключения к базе данных", e);
		}
	}

    /**
     * Запуск Grizzly HTTP сервера.
     */
    public static void startServer() {
		logger.debug("Запуск Grizzly HTTP сервера");

        ResourceConfig resourceConfig = new ResourceConfig().packages("ru.vkulakov.phonex.resources");

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Setup.makeBaseUri()), resourceConfig);
    }

    /**
     * Остановка Grizzly HTTP сервера.
     */
    public static void shutdownServer() {
		logger.debug("Остановка Grizzly HTTP сервера");

        server.shutdownNow();
    }

	/**
	 * Инициализация планировщика и запуск задачи периодического обновления базы номеров телефонов.
	 */
	private static void startLoader() {
		logger.debug("Запуск загрузчика");

		executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(new Loader(), 10, PhonexProperties.getInstance().getIntProperty("loader.interval"), TimeUnit.SECONDS);
	}

	/**
	 * Остановка планировщика и задачи периодического обновления базы номеров телефонов.
	 */
	private static void shutdownLoader() {
		logger.debug("Остановка загрузчика");

		try {
			// Отменяем запущенные операции.
			executorService.shutdownNow();

			// Ждём завершения запущенных задач.
			if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				logger.error("Истекло время ожидания остановки загрузчика");
			}
		} catch (InterruptedException e) {
			logger.error("Ошибка ожидания остановки загрузчика", e);
		}

		logger.debug("Окончание остановки загрузчика");
	}

	/**
     * Главный метод для запуска приложения.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) throws InterruptedException {
    	init();
        startServer();
        startLoader();

		logger.debug("Текущий профиль:    {}", PhonexProperties.getInstance().getProperty("profile"));
		logger.debug("Рабочая директория: {}", System.getProperty("user.dir"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.debug("Получен сигнал завершения приложения");

			shutdownLoader();
			shutdownServer();

			latch.countDown();

			// Пробуем дать возможность нормально завершиться основному потоку.
			Thread.yield();
		}));

		latch.await();

		logger.debug("Завершение приложения");
    }
}
