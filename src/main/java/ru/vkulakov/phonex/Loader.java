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

package ru.vkulakov.phonex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vkulakov.phonex.services.RangeService;
import ru.vkulakov.phonex.utils.PhonexProperties;

import java.util.Map;

/**
 * Загрузка информации о номерах телефонах.
 */
public class Loader implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(Loader.class);

	@Override
	public void run() {
		logger.debug("Запуск загрузчика номеров телефонов");

		RangeService rangeService = new RangeService();
		try {
			Map<String, String> codes = PhonexProperties.getInstance().getPropertyByPrefix("rossvyaz.");
			for(Map.Entry<String, String> code : codes.entrySet()) {
				if(Thread.currentThread().isInterrupted()) {
					logger.debug("Загрузка номеров телефонов прервана");
					break;
				}

				rangeService.load(code.getKey(), code.getValue());
			}
		} catch (Exception e) {
			logger.error("Ошибка загрузки данных с диапазонами номеров телефонов", e);
		}

		logger.debug("Завершение загрузчика номеров телефонов");
	}
}
