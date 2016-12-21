package ru.vkulakov.phonex.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.vkulakov.phonex.dao.PhoneDao;
import ru.vkulakov.phonex.dao.RangeDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.model.Range;
import ru.vkulakov.phonex.utils.Setup;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Сервис для работы с информацией о номерах телефонов.
 */
public class PhoneService {
	public Phone search(String phoneStr) {
		try (
			Connection conn = Setup.getConnection();
		) {
			return new PhoneDao(conn).getByPhone(phoneStr);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка поиска информации по номеру телефона", e);
		}
	}
}
