package ru.vkulakov.phonex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * <h3>Тестирование получения параметров приложения</h3>
 */
public class PhonexPropertiesTest {
	@Before
	public void setUp() {
		Properties properties = new Properties();
		properties.setProperty("prop.string",   "Test string 0");
		properties.setProperty("list.string.1", "Test string 1");
		properties.setProperty("list.string.2", "Test string 2");

		PhonexProperties.getInstance(properties);
	}

	@After
	public void tearDown() {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void getInstance() {
		assertNotNull("Инстанс параметров приложения не получен", PhonexProperties.getInstance());
	}

	@Test
	public void getPropertyExist() {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Test string 0", PhonexProperties.getInstance().getProperty("prop.string"));
	}

	@Test(expected = PhonexProperties.PropertiesException.class)
	public void getPropertyNotExist() {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Test string 0", PhonexProperties.getInstance().getProperty("some.string"));
	}

	@Test
	public void getPropertyWithDefaultExist() {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Test string 0", PhonexProperties.getInstance().getProperty("prop.string", "Default string 0"));
	}

	@Test
	public void getPropertyWithDefaultNotExist() {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Default string 0", PhonexProperties.getInstance().getProperty("some.string", "Default string 0"));
	}

	@Test
	public void getListPropertyExist() {
		Map<String, String> expected = new HashMap<String, String>();
		expected.put("string.1", "Test string 1");
		expected.put("string.2", "Test string 2");

		assertEquals("Полученнное значение не соответствует ожидаемому", expected, PhonexProperties.getInstance().getPropertyByPrefix("list."));
	}

	@Test
	public void getListPropertyNotExist() {
		Map<String, String> expected = new HashMap<String, String>();

		assertEquals("Полученнное значение не соответствует ожидаемому", expected, PhonexProperties.getInstance().getPropertyByPrefix("some."));
	}
}