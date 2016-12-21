package ru.vkulakov.phonex.dao;

import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * ДАО для работы с номерами телефонов.
 */
public class PhoneDao {
	private Connection conn;

	private final static Map<String, String> tables = new HashMap<String, String>();
	static {
		tables.put("3", "code_3kh");
		tables.put("4", "code_4kh");
		tables.put("8", "code_8kh");
		tables.put("9", "code_9kh");
	}

	public PhoneDao(Connection conn) {
		this.conn = conn;
	}

	public Phone getByPhone(String phoneStr) {
		if(phoneStr == null || phoneStr.trim().isEmpty()) {
			return null;
		}

		String p = phoneStr.substring(1, 2);
		String table = tables.getOrDefault(p, null);
		if(table == null) {
			return null;
		}

		int code = Integer.valueOf(phoneStr.substring(1, 4));
		int number = Integer.valueOf(phoneStr.substring(4, 11));

		String query = "" +
				" SELECT" +
				"     c.region AS region," +
				"     c.operator AS operator" +
				" FROM" +
				"     " + table + " c" +
				" WHERE" +
				"     c.code = ? AND" +
				"     c.start <= ? AND ? <= c.finish;";
		try (
			PreparedStatement ps = conn.prepareStatement(query);
		) {
			int index = 1;
			ps.setInt(index++, code);
			ps.setInt(index++, number);
			ps.setInt(index++, number);

			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Phone phone = new Phone();
				phone.setPhone(phoneStr);
				phone.setRegion(rs.getString("region"));
				phone.setOperator(rs.getString("operator"));

				return phone;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new PhonexException("Ошибка выполенния запроса", e);
		}
	}
}
