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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.Main;
import ru.vkulakov.phonex.utils.PhonexPropertiesWrap;

public class RangeServiceTest {
	@Before
	public void setUp() {
		Main.init();
	}

	@After
	public void tearDown() {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void load() {
		new RangeService().load("code_9kh", "file:///" + System.getProperty("user.dir") + "/src/test/resources/RangeServiceTest.csv");
	}
}
