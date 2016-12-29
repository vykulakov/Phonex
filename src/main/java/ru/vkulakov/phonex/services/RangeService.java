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

package ru.vkulakov.phonex.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Сервис для работы с информацией о диапазонах номеров телефонов.
 */
public class RangeService {
	private final static Logger logger = LoggerFactory.getLogger(RangeService.class);

	/**
	 * Формат CSV-файла для парсинга.
	 */
	private final static CSVFormat format = CSVFormat.EXCEL
			.withDelimiter(';')
			.withFirstRecordAsHeader()
			.withSkipHeaderRecord()
			.withIgnoreEmptyLines()
			.withIgnoreSurroundingSpaces()
			.withTrim();

	/**
	 * Загрузка информации о диапазонах номеров телефонов.
	 * @param table таблица для сохранения информации о диапазонах,
	 * @param url URL CSV-файла для чтения информации о диапазонах.
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
					logger.debug("Парсинг номеров телефонов прерван");
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

				if(++i % 5000 == 0) {
					conn.commit();
				}
			}

			long finish = System.currentTimeMillis();
			logger.debug("Завершение загрузки данных в базу [time={}, table={}]", (finish - start), table);

			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new PhonexException("Ошибка получения подключения к базе данных", e);
		} catch (IOException e) {
			throw new PhonexException("Ошибка чтения диапазонов номеров телефонов", e);
		}
	}
}
