package ru.vkulakov.phonex.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <h3>Тестирование получения реальных параметров приложения из файла</h3>
 */
public class PhonexPropertiesRealTest {
	@Before
	public void setUp() throws Exception {
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
		assertTrue("Полученнное значение не соответствует ожидаемому", Arrays.asList("test", "develop").contains(PhonexProperties.getInstance().getProperty("profile")));
	}
}
