package ru.vkulakov.phonex.dao;

import java.sql.Connection;

/**
 * ДАО для работы с номерами телефонов.
 */
public class PhoneDao {
	Connection conn;

	public PhoneDao(Connection conn) {
		this.conn = conn;
	}
}
