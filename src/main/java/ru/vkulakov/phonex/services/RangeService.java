package ru.vkulakov.phonex.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.vkulakov.phonex.dao.RangeDao;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Range;
import ru.vkulakov.phonex.utils.Setup;

import java.io.IOException;
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
	 * Загрузка диапазонов номеров телефонов.
	 * @param table таблица для сохранения диапазонов,
	 * @param url поток для чтения диапазонов.
	 */
	public void load(String table, String url) {
		try (
			Connection conn = Setup.getConnection();
		) {
			RangeDao rangeDao = new RangeDao(conn);

			// Сначала удаляем все записи.
			rangeDao.truncate(table);

			conn.setAutoCommit(false);

			CSVParser parser = CSVParser.parse(new URL(url), Charset.forName("cp1251"), format);

			int i = 0;
			long start = System.currentTimeMillis();
			Range range = new Range();
			for(CSVRecord record : parser) {
				if(Thread.currentThread().isInterrupted()) {
					System.out.println("Парсинг номеров телефонов прерван");
					conn.commit();
					break;
				}

				range.setCode(Integer.valueOf(record.get(0)));
				range.setStart(Integer.valueOf(record.get(1)));
				range.setFinish(Integer.valueOf(record.get(2)));
				range.setCapacity(Integer.valueOf(record.get(3)));
				range.setOperator(record.get(4));
				range.setRegion(record.get(5));

				rangeDao.insert(table, range);

				// Делаем коммит после каждой тысячной записи.
				// Сначала увеличиваем на единицу, а потом проверяем на тысячу.
				if(++i % 5000 == 0) {
					System.out.println(String.format("Commit [i=%d, table=%s]", i, table));
					conn.commit();
				}
			}

			long finish = System.currentTimeMillis();
			System.out.println(String.format("Завершение загрузки данных в базу [time=%d, table=%s]", (finish - start), table));

			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка получения подключения к базе данных", e);
		} catch (IOException e) {
			throw new PhonexException("Ошибка чтения диапазонов номеров телефонов", e);
		}
	}
}
