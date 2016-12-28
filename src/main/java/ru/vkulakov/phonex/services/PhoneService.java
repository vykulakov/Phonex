package ru.vkulakov.phonex.services;

import ru.vkulakov.phonex.dao.PhoneDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.utils.Setup;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Сервис для работы с информацией о номерах телефонов.
 */
public class PhoneService {
	/**
	 * Поиск информации по номеру телефона.
	 * @param phoneStr номер телефона.
	 * @return Информация по номеру телефона или {@code null}, если номер телефона не найден.
	 */
	public Phone search(String phoneStr) {
		try (
			Connection conn = Setup.getConnection()
		) {
			return new PhoneDao(conn).getByPhone(phoneStr);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка поиска информации по номеру телефона", e);
		}
	}
}
