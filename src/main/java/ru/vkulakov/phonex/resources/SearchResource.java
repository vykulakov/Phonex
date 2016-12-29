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
import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.model.Result;
import ru.vkulakov.phonex.services.PhoneService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Ресурс для поиска информации по номеру телефона.
 * Публикует ресурс по указанному пути и определяет методы для
 * обработки соответствующих запросов.
 */
@Path("search/{phone}")
public class SearchResource {
	private final static Logger logger = LoggerFactory.getLogger(SearchResource.class);

	/**
	 * Поиск информации по номеру телефона.
	 * Обрабатывает GET-запросы к ресурсу: проверяет входные параметры
	 * на валидность, ищет номер телефона и возвращает ответ с информацией
	 * по переданному номеру телефона.
	 * @param phoneStr номер телефона.
	 * @return Результат поиска информации по номеру телефона.
	 */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Result search(@PathParam("phone") String phoneStr) {
    	if(phoneStr == null || phoneStr.trim().isEmpty()) {
    		return new Result(Result.EMPTY, "Номер телефона не передан");
		}

    	if(!phoneStr.matches("^7\\d{10}$")) {
			return new Result(Result.BAD_FORMAT, "Передан некорректный номер телефона");
		}

		try {
			Phone phone = new PhoneService().searchByPhone(phoneStr);
			if(phone == null) {
				return new Result(Result.NOT_FOUND, "Информация по номеру телефона не найдена");
			} else {
				return new Result(phone);
			}
		} catch (Exception e) {
			logger.error("Ошибка поиска информации по номеру телефона", e);

			return new Result(Result.ERROR, "Внутренняя ошибка на сервере");
		}
    }
}
