package ru.vkulakov.phonex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * <h3>Тестирование получения параметров приложения</h3>
 */
public class PhonexPropertiesTest {
	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("prop.string", "Test string");

		PhonexProperties.getInstance(properties);
	}

	@After
	public void tearDown() throws Exception {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void getInstance() throws Exception {
		assertNotNull("Инстанс параметров приложения не получен", PhonexProperties.getInstance());
	}

	@Test
	public void getProperty() throws Exception {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Test string", PhonexProperties.getInstance().getProperty("prop.string"));
	}

	@Test(expected = PhonexProperties.PropertiesException.class)
	public void getPropertyNotExist() throws Exception {
		assertEquals("Полученнное значение не соответствует ожидаемому", "Test string", PhonexProperties.getInstance().getProperty("some.string"));
	}
}