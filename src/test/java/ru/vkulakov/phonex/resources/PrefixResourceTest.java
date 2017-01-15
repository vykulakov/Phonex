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

package ru.vkulakov.phonex.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.services.PhoneService;

import javax.ws.rs.core.Application;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование ресурс для поиска информации по номеру телефона.
 */
public class PrefixResourceTest extends JerseyTest {
	@Test
	public void testSearchEmpty() {
		String expected = "{\"message\":\"Префикс номера телефона не передан\",\"result\":1}";
		String actual = target("prefix/ ").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchNormal() {
		String expected = "{\"message\":\"\",\"phone\":{},\"result\":0}";
		String actual = target("prefix/79515639692").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchBadFormat() {
		String expected = "{\"message\":\"Передан некорректный префикс номера телефона\",\"result\":2}";
		String actual = target("prefix/7012345678X").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchNotFound() {
		String expected = "{\"message\":\"Информация по префиксу номера телефона не найдена\",\"result\":3}";
		String actual = target("prefix/70123456789").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchTooShort() {
		String expected = "{\"message\":\"Передан слишком короткий префикс номера телефона\",\"result\":4}";
		String actual = target("prefix/7").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchTooMany() {
		String expected = "{\"message\":\"Найдено более одного номера телефона по префиксу\",\"result\":5}";
		String actual = target("prefix/7951563969").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Override
	protected Application configure() {
		return new ResourceConfig().register(new PrefixResource() {
			@Override
			protected PhoneService createPhoneService() {
				PhoneService phoneService = mock(PhoneService.class);

				when(phoneService.searchByPrefix("79515639692")).thenReturn(Arrays.asList(new Phone()));
				when(phoneService.searchByPrefix("7951563969")).thenReturn(Arrays.asList(new Phone(), new Phone()));

				return phoneService;
			}
		});
	}
}
