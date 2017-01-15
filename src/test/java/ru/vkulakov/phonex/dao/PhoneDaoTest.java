package ru.vkulakov.phonex.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.utils.PhonexProperties;
import ru.vkulakov.phonex.utils.PhonexPropertiesWrap;
import ru.vkulakov.phonex.utils.Setup;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Тестирование ДАО для работы с номерами телефонов.
 */
public class PhoneDaoTest {
	private Connection conn;

	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("db.driver", "org.sqlite.JDBC");
		properties.setProperty("db.url", "jdbc:sqlite:data/phonex.test.db");
		properties.setProperty("prefix.8", "table8");
		properties.setProperty("prefix.9", "table9");

		PhonexProperties.getInstance(properties);

		conn = Setup.getConnection();

		Setup.initDatabase(conn);
	}

	@After
	public void tearDown() throws Exception {
		conn.close();

		PhonexPropertiesWrap.recycle();

		Files.delete(Paths.get(System.getProperty("user.dir"), "data/phonex.test.db"));
	}

	@Test
	public void getByPhone() throws Exception {
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (951, 5000000, 5999999, 1000000, 'Регион 1', 'Оператор 1')");

		assertNotNull(new PhoneDao(conn).getByPhone("79515639692"));
	}

	@Test
	public void getByPhoneNotFound() throws Exception {
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (951, 6000000, 6999999, 1000000, 'Регион 1', 'Оператор 1')");

		assertNull(new PhoneDao(conn).getByPhone("79515639692"));
	}

	@Test
	public void listByCodeRange() throws Exception {
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (951, 5639690, 5639699, 10, 'Регион 1', 'Оператор 1')");
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (960, 1000000, 9999999, 8000000, 'Регион 1', 'Оператор 1')");

		assertEquals(0, new PhoneDao(conn).listByCodeRange(2, 910, 919).size());
		assertEquals(2, new PhoneDao(conn).listByCodeRange(2, 950, 959).size());
		assertEquals(10, new PhoneDao(conn).listByCodeRange(10, 950, 959).size());
		assertEquals(10, new PhoneDao(conn).listByCodeRange(20, 950, 959).size());
		assertEquals(20, new PhoneDao(conn).listByCodeRange(20, 960, 969).size());
	}

	@Test
	public void listByNumberRange() throws Exception {
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (951, 5639690, 5639699, 10, 'Регион 1', 'Оператор 1')");
		conn.createStatement().executeUpdate("INSERT INTO table9 VALUES (960, 1000000, 9999999, 8000000, 'Регион 1', 'Оператор 1')");

		assertEquals(0, new PhoneDao(conn).listByNumberRange(2, 910, 0, 9999999).size());
		assertEquals(1, new PhoneDao(conn).listByNumberRange(2, 951, 5639692, 5639692).size());
		assertEquals(2, new PhoneDao(conn).listByNumberRange(2, 951, 5639690, 5639699).size());
		assertEquals(10, new PhoneDao(conn).listByNumberRange(10, 951, 5639690, 5639699).size());
		assertEquals(10, new PhoneDao(conn).listByNumberRange(20, 951, 5639690, 5639699).size());
		assertEquals(20, new PhoneDao(conn).listByNumberRange(20, 960, 5639690, 5639799).size());
	}
}