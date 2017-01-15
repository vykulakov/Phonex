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

package ru.vkulakov.phonex.services;

import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.dao.RangeDao;
import ru.vkulakov.phonex.model.Range;

import java.sql.Connection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RangeServiceTest {
	private RangeDao rangeDao;
	private RangeService rangeService;

	@Before
	public void setUp() {
		rangeDao = mock(RangeDao.class);

		rangeService = new RangeService() {
			@Override
			protected Connection createConnection() {
				return mock(Connection.class);
			}

			@Override
			protected RangeDao createRangeDao(Connection conn) {
				return rangeDao;
			}
		};
	}

	@Test
	public void load() {
		rangeService.load("table", "file:///" + System.getProperty("user.dir") + "/src/test/resources/RangeServiceTest.csv");

		verify(rangeDao, times(1)).truncate(eq("table"));
		verify(rangeDao, times(58)).insert(eq("table"), any(Range.class));
	}
}
