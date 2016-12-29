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
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Тестирование класса для получения доступа к ресурсам приложения.
 */
public class SetupTest {
	@After
	public void tearDown() {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void getConnection() {
		Properties properties = new Properties();
		properties.setProperty("db.driver", "org.sqlite.JDBC");
		properties.setProperty("db.url", "jdbc:sqlite:data/phonex.test.db");

		PhonexProperties.getInstance(properties);

		assertNotNull(Setup.getConnection());
	}

	@Test
	public void initDatabase() {
		Properties properties = new Properties();
		properties.setProperty("db.driver", "org.sqlite.JDBC");
		properties.setProperty("db.url", "jdbc:sqlite:data/phonex.test.db");

		PhonexProperties.getInstance(properties);

		Setup.initDatabase(Setup.getConnection());
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
