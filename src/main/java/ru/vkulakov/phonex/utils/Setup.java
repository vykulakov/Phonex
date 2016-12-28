package ru.vkulakov.phonex.utils;

import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.exceptions.PropertiesException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Класс для получения доступа к ресурсам приложения
 */
public class Setup {
	/**
	 * Получение подключения к базе данных.
	 * @return Подключение к базе данных.
	 */
	public static Connection getConnection() {
		Connection connection;
		try {
			Class.forName(PhonexProperties.getInstance().getProperty("db.driver"));
			connection = DriverManager.getConnection(PhonexProperties.getInstance().getProperty("db.url"));
		} catch (PropertiesException e) {
			throw new PhonexException("Ошибка получения параметров для подключения к базе данных", e);
		} catch (ClassNotFoundException e) {
			throw new PhonexException("Драйвер для подключения к базе данных не найден", e);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка подключения к базе данных", e);
		}

		return connection;
	}

	/**
	 * Инициализации базы данных.
	 * Создаёт необходимые для работы таблицы в базе данных.
	 * @param conn подключение к базе данных.
	 */
	public static void initDatabase(Connection conn) {
		try {
			Statement stmt = conn.createStatement();

			Map<String, String> codes = PhonexProperties.getInstance().getPropertyByPrefix("rossvyaz.");
			for(String table : codes.keySet()) {
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (code INTEGER, start INTEGER, finish INTEGER, capacity INTEGER, operator STRING, region STRING)");
			}
		} catch (SQLException e) {
			throw new PhonexException("Ошибка инициализации базы данных", e);
		}
	}

	/**
	 * Формирование базового адреса сервера.
	 * Получает параметры приложения и из них составляет базовый адрес,
	 * который будет слушать сервер.
	 * @return Базовый адрес сервера.
	 */
	public static String makeBaseUri() {
		String host = PhonexProperties.getInstance().getProperty("listen.host", "127.0.0.1");
		String port = PhonexProperties.getInstance().getProperty("listen.port", "8080");
		String path = PhonexProperties.getInstance().getProperty("listen.path", "/phonex");

		if(!path.startsWith("/")) {
			path = "/" + path;
		}
		if(!path.endsWith("/")) {
			path = path + "/";
		}

		String result = "";
		result += "http://";
		result += host;
		result += ":";
		result += port;
		result += path;

		return result;
	}
}
