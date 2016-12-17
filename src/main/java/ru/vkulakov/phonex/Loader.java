package ru.vkulakov.phonex;

import ru.vkulakov.phonex.services.RangeService;

import java.util.Map;

/**
 * Загрузка информации в базу данных.
 */
public class Loader implements Runnable {
	@Override
	public void run() {
		System.out.println("Запуск загрузчика номеров телефонов");

		RangeService rangeService = new RangeService();
		try {
			Map<String, String> codes = PhonexProperties.getInstance().getPropertyByPrefix("rossvyaz.");
			for(Map.Entry<String, String> code : codes.entrySet()) {
				rangeService.load(code.getKey(), code.getValue());
			}
		} catch (Exception e) {
			System.err.println("Ошибка загрузки данных с диапазонами номеров телефонов");
			e.printStackTrace(System.err);
		}

		System.out.println("Завершение загрузчика номеров телефонов");
	}
}
