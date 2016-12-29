package ru.vkulakov.phonex.services;

import ru.vkulakov.phonex.dao.PhoneDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.utils.Setup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы с информацией о номерах телефонов.
 */
public class PhoneService {
	/**
	 * Поиск информации по номеру телефона.
	 * @param phoneStr номер телефона.
	 * @return Информация по номеру телефона или {@code null}, если номер телефона не найден.
	 */
	public Phone searchByPhone(String phoneStr) {
		try (
			Connection conn = Setup.getConnection()
		) {
			return new PhoneDao(conn).getByPhone(phoneStr);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка поиска информации по номеру телефона", e);
		}
	}

	/**
	 * Поиск информации по префиксу номера телефона.
	 * @param prefixStr префикс номера телефона.
	 * @return Список найденных номеров телефонов.
	 */
	public List<Phone> searchByPrefix(String prefixStr) {
		try (
			Connection conn = Setup.getConnection();
		) {
			if(prefixStr.matches("^7\\d{1,3}$")) {
				String code = prefixStr.substring(1);
				int codeStart = Integer.valueOf(code + "000".substring(code.length()));
				int codeFinish = Integer.valueOf(code + "999".substring(code.length()));

				return new PhoneDao(conn).listByCodeRange(2, codeStart, codeFinish);
			}
			if(prefixStr.matches("^7\\d{4,10}$")) {
				int code = Integer.valueOf(prefixStr.substring(1, 4));

				String number = prefixStr.substring(4);
				int numberStart = Integer.valueOf(number + "0000000".substring(number.length()));
				int numberFinish = Integer.valueOf(number + "9999999".substring(number.length()));

				return new PhoneDao(conn).listByNumberRange(2, code, numberStart, numberFinish);
			}
		} catch (SQLException e) {
			throw new PhonexException("Ошибка поиска информации по префиксу номера телефона", e);
		}

		return Collections.emptyList();
	}
}
