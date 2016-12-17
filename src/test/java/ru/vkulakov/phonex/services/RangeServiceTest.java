package ru.vkulakov.phonex.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.Main;
import ru.vkulakov.phonex.PhonexPropertiesWrap;

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
		new RangeService().load("code_9kh", "file:///" + System.getProperty("user.dir") + "/src/test/java/ru/vkulakov/phonex/services/RangeServiceTest.csv");
	}
}
