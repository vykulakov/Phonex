/*
 * Phonex - https://github.com/vykulakov/Phonex
 *
 * Copyright 2016 Vyacheslav Kulakov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vkulakov.phonex.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.exceptions.PropertiesException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	@Test(expected = PropertiesException.class)
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