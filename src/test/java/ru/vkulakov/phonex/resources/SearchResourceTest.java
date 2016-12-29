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

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.Main;
import ru.vkulakov.phonex.services.RangeService;
import ru.vkulakov.phonex.utils.PhonexPropertiesWrap;
import ru.vkulakov.phonex.utils.Setup;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class SearchResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        Main.startServer();

        Client c = ClientBuilder.newClient();

        target = c.target(Setup.makeBaseUri());
    }

    @After
    public void tearDown() throws Exception {
        Main.shutdownServer();
		PhonexPropertiesWrap.recycle();
    }

	@Test
	public void testSearchEmpty() {
		String expected = "{\"message\":\"Номер телефона не передан\",\"result\":1}";
		String actual = target.path("search/ ").request().get(String.class);
		assertEquals(expected, actual);
	}

    @Test
    public void testSearchNormal() {
		new RangeService().load("code_9kh", "file:///" + System.getProperty("user.dir") + "/src/test/resources/RangeServiceTest.csv");

		String expected = "{\"message\":\"\",\"phone\":{\"operator\":\"ООО \\\"Т2 Мобайл\\\"\",\"phone\":\"79515639692\",\"region\":\"Воронежская обл.\"},\"result\":0}";
        String actual = target.path("search/79515639692").request().get(String.class);
        assertEquals(expected, actual);
    }

	@Test
	public void testSearchNotFound() {
		String expected = "{\"message\":\"Информация по номеру телефона не найдена\",\"result\":3}";
		String actual = target.path("search/70123456789").request().get(String.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testSearchBadFormat() {
		String expected = "{\"message\":\"Передан некорректный номер телефона\",\"result\":2}";
		String actual = target.path("search/7012345678X").request().get(String.class);
		assertEquals(expected, actual);
	}
}
