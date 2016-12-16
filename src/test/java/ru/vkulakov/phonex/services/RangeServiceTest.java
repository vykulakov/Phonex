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
		new RangeService().load("https://www.rossvyaz.ru/docs/articles/Kody_DEF-9kh.csv");
	}
}
