package ru.vkulakov.phonex;

import ru.vkulakov.phonex.services.RangeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Загрузка информации в базу данных.
 */
public class Loader implements Runnable {
	private final static String CODE_9kh = "https://www.rossvyaz.ru/docs/articles/Kody_DEF-9kh.csv";

	@Override
	public void run() {
		RangeService rangeService = new RangeService();
		try {
			rangeService.load(CODE_9kh);
		} catch (Exception e) {
			System.err.println("Ошибка загрузки данных с диапазонами номеров телефонов");
			e.printStackTrace(System.err);
		}
	}
}
