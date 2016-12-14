package ru.vkulakov.phonex.utils;

import ru.vkulakov.phonex.PhonexProperties;
import ru.vkulakov.phonex.exceptions.PhonexException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <h3>Класс для получения доступа к ресурсам приложения</h3>
 */
public class Setup {
	/**
	 * <p>Получение подключения к базе данных.</p>
	 * @return Подключение к базе данных.
	 */
	public static Connection getConnection() {
		Connection connection;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:data/phonex.db");
		} catch (ClassNotFoundException e) {
			throw new PhonexException("Драйвер для подключения к базе данных не найден", e);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка подключения к базе данных", e);
		}

		return connection;
	}

	/**
	 * <p>Инициализации базы данных.</p>
	 * <p>Создаёт необходимые для работы таблицы в базе данных.</p>
	 * @param conn подключение к базе данных.
	 */
	public static void initDatabase(Connection conn) {
		try {
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS code_9kh (code INTEGER, start INTEGER, finish INTEGER, capacity INTEGER, operator STRING, region STRING)");
		} catch (SQLException e) {
			throw new PhonexException("Ошибка инициализации базы данных", e);
		}
	}

	/**
	 * <p>Формирование базового адреса сервера.</p>
	 * <p>Получает параметры приложения и из них составляет базовый адрес,
	 * который будет слушать сервер.</p>
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

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("http://");
		stringBuilder.append(host);
		stringBuilder.append(":");
		stringBuilder.append(port);
		stringBuilder.append(path);

		return stringBuilder.toString();
	}
}
