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

package ru.vkulakov.phonex.dao;

import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.utils.PhonexProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ДАО для работы с номерами телефонов.
 */
public class PhoneDao {
	private Connection conn;

	public PhoneDao(Connection conn) {
		this.conn = conn;
	}

	public Phone getByPhone(String phoneStr) {
		if(phoneStr == null || phoneStr.trim().isEmpty()) {
			return null;
		}

		String prefix = phoneStr.substring(1, 2);
		String table = PhonexProperties.getInstance().getProperty("prefix." + prefix, null);
		if(table == null) {
			return null;
		}

		int code = Integer.valueOf(phoneStr.substring(1, 4));
		int number = Integer.valueOf(phoneStr.substring(4, 11));

		String query = "" +
				" SELECT" +
				"     c.code AS code," +
				"     c.start AS start," +
				"     c.finish AS finish," +
				"     c.region AS region," +
				"     c.operator AS operator" +
				" FROM" +
				"     " + table + " c" +
				" WHERE" +
				"     c.code = ? AND" +
				"     c.start <= ? AND ? <= c.finish;";
		try (
			PreparedStatement ps = conn.prepareStatement(query)
		) {
			int index = 1;
			ps.setInt(index++, code);
			ps.setInt(index++, number);
			ps.setInt(index++, number);

			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return getPhoneFromRs(phoneStr, rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new PhonexException("Ошибка выполенния запроса", e);
		}
	}

	/**
	 * Получение списка номеров телефонов по диапазону кодов.
	 * @param limit максимальное количество номеров телефонов, которое может вернуть метод,
	 * @param codeStart начало диапазона кодов,
	 * @param codeFinish конец диапазона кодов.
	 * @return Список номеров телефонов.
	 */
	public List<Phone> listByCodeRange(int limit, int codeStart, int codeFinish) {
		String prefix = ("" + codeStart).substring(0, 1);
		String table = PhonexProperties.getInstance().getProperty("prefix." + prefix, null);
		if(table == null) {
			return Collections.emptyList();
		}

		String query = "" +
			" SELECT" +
			"     c.code AS code," +
			"     c.start AS start," +
			"     c.finish AS finish," +
			"     c.region AS region," +
			"     c.operator AS operator" +
			" FROM" +
			"     " + table + " c" +
			" WHERE" +
			"     ? <= c.code AND c.code <= ?" +
			" LIMIT ?;";
		try (
			PreparedStatement ps = conn.prepareStatement(query);
		) {
			int index = 1;
			ps.setInt(index++, codeStart);
			ps.setInt(index++, codeFinish);
			ps.setInt(index++, limit);

			ResultSet rs = ps.executeQuery();

			List<Phone> phones = new ArrayList<Phone>();
			while(rs.next()) {
				Phone phone = getPhoneFromRs(null, rs);
				fill(limit, phone.getCode(), phone.getStart(), phone.getFinish(), phone, phones);

				if(phones.size() >= limit) {
					break;
				}
			}

			return phones;
		} catch (SQLException e) {
			throw new PhonexException("Ошибка выполенния запроса", e);
		}
	}

	/**
	 * Получение списка номеров телефонов по диапазону номеров.
	 * @param limit максимальное количество номеров телефонов, которое может вернуть метод,
	 * @param code код для диапазона номеров,
	 * @param numberStart начало диапазона номеров,
	 * @param numberFinish конец диапазона номеров.
	 * @return Список номеров телефонов.
	 */
	public List<Phone> listByNumberRange(int limit, int code, int numberStart, int numberFinish) {
		String prefix = ("" + code).substring(0, 1);
		String table = PhonexProperties.getInstance().getProperty("prefix." + prefix, null);
		if(table == null) {
			return Collections.emptyList();
		}

		String query = "" +
			" SELECT" +
			"     c.code AS code," +
			"     c.start AS start," +
			"     c.finish AS finish," +
			"     c.region AS region," +
			"     c.operator AS operator" +
			" FROM" +
			"     " + table + " c" +
			" WHERE" +
			"     c.code = ? AND" +
			"     ? <= c.finish AND c.start <= ?" +
			" LIMIT ?;";
		try (
			PreparedStatement ps = conn.prepareStatement(query);
		) {
			int index = 1;
			ps.setInt(index++, code);
			ps.setInt(index++, numberStart);
			ps.setInt(index++, numberFinish);
			ps.setInt(index++, limit);

			ResultSet rs = ps.executeQuery();

			List<Phone> phones = new ArrayList<Phone>();
			while(rs.next()) {
				Phone phone = getPhoneFromRs(null, rs);
				fill(limit, phone.getCode(), Math.max(numberStart, phone.getStart()), Math.min(numberFinish, phone.getFinish()), phone, phones);

				if(phones.size() >= limit) {
					break;
				}
			}

			return phones;
		} catch (SQLException e) {
			throw new PhonexException("Ошибка выполенния запроса", e);
		}
	}

	/**
	 * Заполняет список номерами телефонов, сгенерированными с указанным кодом и диапазонами номеров.
	 * @param limit ограничение для размера списка номеров телефонов,
	 * @param code код для генерации номера телефона,
	 * @param start начало диапазона для генерации номера телефона,
	 * @param finish конец диапазона для генерации номера телефона,
	 * @param phone объект, на основе которого будут строится другие объекты для добавления их в список,
	 * @param phones список, в который будут добавляться все сгенерированный номера телефонов.
	 */
	private void fill(int limit, int code, int start, int finish, Phone phone, List<Phone> phones) {
		for(int i  = start; i <= finish; i++) {
			String number = "" + i;

			Phone clone = phone.clone();
			clone.setPhone("7" + code + "0000000".substring(number.length()) + number);
			phones.add(clone);

			if(phones.size() >= limit) {
				break;
			}
		}
	}

	/**
	 * Получение объекта с информацией о номере телефона из ResultSet.
	 * @param phoneStr номер телефона,
	 * @param resultSet ResultSet с установленной текущей строкой.
	 * @return Объект с информацией о номере телефона.
	 * @throws SQLException
	 */
	private Phone getPhoneFromRs(String phoneStr, ResultSet resultSet) throws SQLException {
		Phone phone = new Phone();
		phone.setCode(resultSet.getInt("code"));
		phone.setStart(resultSet.getInt("start"));
		phone.setFinish(resultSet.getInt("finish"));
		phone.setPhone(phoneStr);
		phone.setRegion(resultSet.getString("region"));
		phone.setOperator(resultSet.getString("operator"));

		return phone;
	}
}
