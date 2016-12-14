package ru.vkulakov.phonex.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import ru.vkulakov.phonex.PhonexProperties;
import ru.vkulakov.phonex.PhonexPropertiesWrap;

import java.util.Properties;

/**
 * Тестирование класса для получения доступа к ресурсам приложения.
 */
public class SetupTest {
	@After
	public void tearDown() {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void makeBaseUriDefault() {
		Properties properties = new Properties();

		PhonexProperties.getInstance(properties);

		assertEquals("http://127.0.0.1:8080/phonex/", Setup.makeBaseUri());
	}

	@Test
	public void makeBaseUriPath1() {
		Properties properties = new Properties();
		properties.setProperty("listen.path", "test");

		PhonexProperties.getInstance(properties);

		assertEquals("http://127.0.0.1:8080/test/", Setup.makeBaseUri());
	}

	@Test
	public void makeBaseUriPath2() {
		Properties properties = new Properties();
		properties.setProperty("listen.path", "test/");

		PhonexProperties.getInstance(properties);

		assertEquals("http://127.0.0.1:8080/test/", Setup.makeBaseUri());
	}

	@Test
	public void makeBaseUriCustom() {
		Properties properties = new Properties();
		properties.setProperty("listen.host", "test.ru");
		properties.setProperty("listen.port", "8888");
		properties.setProperty("listen.path", "/test");

		PhonexProperties.getInstance(properties);

		assertEquals("http://test.ru:8888/test/", Setup.makeBaseUri());
	}
}
