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
			Connection conn = createConnection()
		) {
			return createPhoneDao(conn).getByPhone(phoneStr);
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
			Connection conn = createConnection()
		) {
			if(prefixStr.matches("^7\\d{1,3}$")) {
				String code = prefixStr.substring(1);
				int codeStart = Integer.valueOf(code + "000".substring(code.length()));
				int codeFinish = Integer.valueOf(code + "999".substring(code.length()));

				return createPhoneDao(conn).listByCodeRange(2, codeStart, codeFinish);
			}
			if(prefixStr.matches("^7\\d{4,10}$")) {
				int code = Integer.valueOf(prefixStr.substring(1, 4));

				String number = prefixStr.substring(4);
				int numberStart = Integer.valueOf(number + "0000000".substring(number.length()));
				int numberFinish = Integer.valueOf(number + "9999999".substring(number.length()));

				return createPhoneDao(conn).listByNumberRange(2, code, numberStart, numberFinish);
			}
		} catch (SQLException e) {
			throw new PhonexException("Ошибка поиска информации по префиксу номера телефона", e);
		}

		return Collections.emptyList();
	}

	/**
	 * Фабричный метод для создания подключения к базе данных.
	 * @return Подключение к базе данных.
	 */
	protected Connection createConnection() {
		return Setup.getConnection();
	}

	/**
	 * Фабричный метод для создания дао для номеров телефонов.
	 * @param conn подключение к базе данных.
	 * @return Дао для номеров телефонов.
	 */
	protected PhoneDao createPhoneDao(Connection conn) {
		return new PhoneDao(conn);
	}
}
