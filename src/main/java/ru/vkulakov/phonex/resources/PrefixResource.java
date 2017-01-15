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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vkulakov.phonex.exceptions.PhonexException;
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.model.Result;
import ru.vkulakov.phonex.services.PhoneService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Ресурс для поиска информации по номеру телефона.
 * Публикует ресурс по указанному пути и определяет методы для
 * обработки соответствующих запросов.
 */
@Path("prefix/{prefix}")
public class PrefixResource {
	private final static Logger logger = LoggerFactory.getLogger(PrefixResource.class);

	/**
	 * Поиск информации по префиксу номера телефона.
	 * Обрабатывает GET-запросы к ресурсу: проверяет входные параметры
	 * на валидность, проверяет существование номеров телефона по префиксу и возвращает
	 * ответ с информацией по переданному префиксу номера телефона.
	 * @param prefixStr префикс номера телефона.
	 * @return Результат поиска информации по номеру телефона.
	 */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Result search(@PathParam("prefix") String prefixStr) {
    	if(prefixStr == null || prefixStr.trim().isEmpty()) {
    		return new Result(Result.EMPTY, "Префикс номера телефона не передан");
		}

		if(!prefixStr.matches("^7\\d{0,10}$")) {
			return new Result(Result.BAD_FORMAT, "Передан некорректный префикс номера телефона");
		}

		if(!prefixStr.matches("^7\\d{1,10}$")) {
			return new Result(Result.TOO_SHORT, "Передан слишком короткий префикс номера телефона");
		}

		try {
			List<Phone> phones = createPhoneService().searchByPrefix(prefixStr);

			if(phones.size() == 0) {
				return new Result(Result.NOT_FOUND, "Информация по префиксу номера телефона не найдена");
			}
			if(phones.size() == 1) {
				return new Result(phones.get(0));
			}
			if(phones.size() >= 2) {
				return new Result(Result.TOO_MANY, "Найдено более одного номера телефона по префиксу");
			}

			throw new PhonexException("Некорректный размер списка номеров телефонов: " + phones.size());
		} catch (Exception e) {
			logger.error("Ошибка поиска информации по префиксу номера телефона", e);

			return new Result(Result.ERROR, "Внутренняя ошибка на сервере");
		}
    }

	/**
	 * Фабричный метод для создания экземпляра сервиса для работы с номерами телефонов.
	 * @return Экземпляр сервиса для работы с номерами телефонов.
	 */
    protected PhoneService createPhoneService() {
    	return new PhoneService();
	}
}
