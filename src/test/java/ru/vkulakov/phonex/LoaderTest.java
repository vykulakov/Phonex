package ru.vkulakov.phonex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.vkulakov.phonex.services.RangeService;
import ru.vkulakov.phonex.utils.PhonexProperties;
import ru.vkulakov.phonex.utils.PhonexPropertiesWrap;

import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * Тестирование загрузки информации о номерах телефонах.
 */
public class LoaderTest {
	private RangeService rangeService;
	private Loader loader;

	@Before
	public void setUp() {
		rangeService = mock(RangeService.class);

		loader = new Loader() {
			@Override
			protected RangeService createRangeService() {
				return rangeService;
			}
		};

		Properties properties = new Properties();
		properties.setProperty("rossvyaz.table8", "url8");
		properties.setProperty("rossvyaz.table9", "url9");

		PhonexProperties.getInstance(properties);
	}

	@After
	public void tearDown() throws Exception {
		PhonexPropertiesWrap.recycle();
	}

	@Test
	public void run() throws Exception {
		loader.run();

		verify(rangeService, times(1)).load("table8", "url8");
		verify(rangeService, times(1)).load("table9", "url9");
	}

}