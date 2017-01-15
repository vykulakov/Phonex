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
import ru.vkulakov.phonex.dao.PhoneDao;
import ru.vkulakov.phonex.model.Phone;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PhoneServiceTest {
	private PhoneDao phoneDao;
	private PhoneService phoneService;

	@Before
	public void setUp() {
		phoneDao = mock(PhoneDao.class);

		phoneService = new PhoneService() {
			@Override
			protected Connection createConnection() {
				return mock(Connection.class);
			}

			@Override
			protected PhoneDao createPhoneDao(Connection conn) {
				return phoneDao;
			}
		};
	}

	@Test
	public void searchByPhone() {
		Phone phone = new Phone();

		when(phoneDao.getByPhone("79515639692")).thenReturn(phone);

		assertEquals(phone, phoneService.searchByPhone("79515639692"));

		verify(phoneDao, times(1)).getByPhone("79515639692");
	}

	@Test
	public void searchByPrefixWithCodeRange() {
		List<Phone> phones = Arrays.asList(new Phone(), new Phone());

		when(phoneDao.listByCodeRange(2, 950, 959)).thenReturn(phones);

		assertEquals(phones, phoneService.searchByPrefix("795"));

		verify(phoneDao, times(1)).listByCodeRange(2, 950, 959);
	}

	@Test
	public void searchByPrefixWithNumberRange() {
		List<Phone> phones = Arrays.asList(new Phone(), new Phone());

		when(phoneDao.listByNumberRange(2, 951, 5630000, 5639999)).thenReturn(phones);

		assertEquals(phones, phoneService.searchByPrefix("7951563"));

		verify(phoneDao, times(1)).listByNumberRange(2, 951, 5630000, 5639999);
	}
}
