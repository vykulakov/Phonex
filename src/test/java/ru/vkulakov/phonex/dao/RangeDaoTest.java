package ru.vkulakov.phonex.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.model.Range;
import ru.vkulakov.phonex.utils.PhonexProperties;
import ru.vkulakov.phonex.utils.PhonexPropertiesWrap;
import ru.vkulakov.phonex.utils.Setup;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;

/**
 * Тестирование ДАО для работы с информацией о диапазонах номеров телефонов.
 */
public class RangeDaoTest {
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
	public void insert() throws Exception {
		Range range8 = new Range();
		range8.setCode(951);
		range8.setStart(5000000);
		range8.setFinish(5999999);
		range8.setCapacity(1000000);
		range8.setRegion("Регион 8");
		range8.setOperator("Оператор 8");

		Range range9 = new Range();
		range9.setCode(952);
		range9.setStart(6000000);
		range9.setFinish(7999999);
		range9.setCapacity(2000000);
		range9.setRegion("Регион 9");
		range9.setOperator("Оператор 9");

		new RangeDao(conn).insert("table8", range8);
		new RangeDao(conn).insert("table9", range9);
	}

	@Test
	public void truncate() throws Exception {
		new RangeDao(conn).truncate("table8");
		new RangeDao(conn).truncate("table9");
	}
}