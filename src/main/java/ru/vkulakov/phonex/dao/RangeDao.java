package ru.vkulakov.phonex.dao;

import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Range;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ДАО для работы с диапазонами номеров телефонов.
 */
public class RangeDao {
	Connection conn;

	public RangeDao(Connection conn) {
		this.conn = conn;
	}

	public void insert(Range range) {
		try (
			PreparedStatement ps = conn.prepareStatement("INSERT INTO code_9kh (code, start, finish, capacity, operator, region) VALUES (?, ?, ?, ?, ?, ?);");
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

	public void truncate() {
		try (
			PreparedStatement ps = conn.prepareStatement("DELETE FROM code_9kh; VACUUM;");
		) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PhonexException("Ошибка очистки таблицы с номерами телефонов", e);
		}
	}
}
