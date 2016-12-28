package ru.vkulakov.phonex.dao;

import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Range;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ДАО для работы с информацией о диапазонах номеров телефонов.
 */
public class RangeDao {
	private Connection conn;

	public RangeDao(Connection conn) {
		this.conn = conn;
	}

	public void insert(String table, Range range) {
		try (
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + " (code, start, finish, capacity, operator, region) VALUES (?, ?, ?, ?, ?, ?)");
		) {
			int index = 1;
			ps.setInt(index++, range.getCode());
			ps.setInt(index++, range.getStart());
			ps.setInt(index++, range.getFinish());
			ps.setInt(index++, range.getCapacity());
			ps.setString(index++, range.getOperator());
			ps.setString(index++, range.getRegion());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PhonexException("Ошибка добавления диапазона номеров телефонов", e);
		}
	}

	public void truncate(String table) {
		try (
			PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + "; VACUUM;");
		) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PhonexException("Ошибка очистки таблицы с диапазонами номеров телефонов", e);
		}
	}
}
