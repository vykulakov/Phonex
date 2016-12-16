package ru.vkulakov.phonex.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.vkulakov.phonex.dao.RangeDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Range;
import ru.vkulakov.phonex.utils.Setup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Сервис для работы с диапазонами номеров телефонов.
 */
public class RangeService {
	private final static CSVFormat format = CSVFormat.EXCEL
			.withDelimiter(';')
			.withFirstRecordAsHeader()
			.withSkipHeaderRecord()
			.withIgnoreEmptyLines()
			.withIgnoreSurroundingSpaces()
			.withTrim();

	/**
	 * Загрузка диапазонов номеров телефонов из
	 * @param url поток для чтения диапазонов.
	 */
	public void load(String url) {
		try (
			Connection conn = Setup.getConnection();
		) {
			RangeDao rangeDao = new RangeDao(conn);

			// Сначала удаляем все записи.
			rangeDao.truncate();

			conn.setAutoCommit(false);

			int i = 0;
			Range range = new Range();
			CSVParser parser = CSVParser.parse(new URL(url), Charset.forName("cp1251"), format);
			for(CSVRecord record : parser) {
				range.setCode(Integer.valueOf(record.get(0)));
				range.setStart(Integer.valueOf(record.get(1)));
				range.setFinish(Integer.valueOf(record.get(2)));
				range.setCapacity(Integer.valueOf(record.get(3)));
				range.setOperator(record.get(4));
				range.setRegion(record.get(5));

				rangeDao.insert(range);

				// Делаем коммит после каждой тысячной записи.
				if(++i % 1000 == 0) {
					System.out.println(String.format("Commit: [i=%d]", i));
					conn.commit();
				}
			}

			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка получения подключения к базе данных", e);
		} catch (IOException e) {
			throw new PhonexException("Ошибка чтения диапазонов номеров телефонов", e);
		}
	}
}
