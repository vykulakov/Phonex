package ru.vkulakov.phonex.services;

import ru.vkulakov.phonex.dao.RangeDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.utils.Setup;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Сервис для работы с диапазонами номеров телефонов.
 */
public class RangeService {
	/**
	 * Загрузка диапазонов номеров телефонов из
	 * @param reader поток для чтения диапазонов.
	 */
	public void load(Reader reader) {
		try (
			Connection conn = Setup.getConnection();
		) {
			RangeDao rangeDao = new RangeDao(conn);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка получения подключения к базе данных", e);
		}
	}
}
