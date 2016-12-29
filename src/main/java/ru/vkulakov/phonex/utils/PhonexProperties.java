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

package ru.vkulakov.phonex.utils;

import ru.vkulakov.phonex.exceptions.PropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Получение параметров приложения
 */
public class PhonexProperties {
	protected Properties properties;

	protected static PhonexProperties instance;

	protected PhonexProperties() {
		try {
			InputStream stream = Files.newInputStream(Paths.get("conf", "phonex.properties"));

			this.properties = new Properties();
			this.properties.load(stream);
		} catch (IOException e) {
			throw new PropertiesException("Ошибка при загрузке файла 'phonex.properties'", e);
		}
	}

	protected PhonexProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Получение значения параметра по его имени.
	 * Если параметр не найден, то выбрасывается {@link PropertiesException}.
	 * @param name имя параметра.
	 * @return Значение параметра.
	 * @throws PropertiesException если параметр с указанным именем в настройках не найден.
	 */
	public String getProperty(String name) {
		if(properties.containsKey(name)) {
			return properties.getProperty(name);
		} else {
			throw new PropertiesException("Параметр '" + name + "' не найден");
		}
	}

	/**
	 * Получение значения параметра по его имени.
	 * Если параметр не найден, то возвращается значение по-умолчанию.
	 * @param name имя параметра.
	 * @param defValue значение по-умолчанию для параметра.
	 * @return Значение параметра или значение по-умолчанию.
	 */
	public String getProperty(String name, String defValue) {
		return properties.getProperty(name, defValue);
	}

	/**
	 * Получение целочисленного значения параметра по его имени.
	 * Если параметр не найден, то выбрасывается {@link PropertiesException}.
	 * @param name имя параметра.
	 * @return Значение параметра.
	 * @throws PropertiesException если параметр с указанным именем в настройках не найден.
	 */
	public int getIntProperty(String name) {
		if(properties.containsKey(name)) {
			return Integer.valueOf(properties.getProperty(name));
		} else {
			throw new PropertiesException("Параметр '" + name + "' не найден");
		}
	}

	/**
	 * Получение целочисленного значения параметра по его имени.
	 * Если параметр не найден, то возвращается значение по-умолчанию.
	 * @param name имя параметра.
	 * @param defValue значение по-умолчанию для параметра.
	 * @return Значение параметра или значение по-умолчанию.
	 */
	public int getIntProperty(String name, int defValue) {
		if(properties.containsKey(name)) {
			return Integer.valueOf(properties.getProperty(name));
		} else {
			return defValue;
		}
	}

	/**
	 * Получение карты значений по префиксу имени
	 * @param prefix префикс имени параметра.
	 * @return Карта значений, где ключ - имя параметра без префикса, значение - значение параметра.
	 */
	public Map<String, String> getPropertyByPrefix(String prefix) {
		Map<String, String> result = new HashMap<String, String>();

		Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			if(name.startsWith(prefix)) {
				result.put(name.replaceFirst(prefix, ""), properties.getProperty(name));
			}
		}
		return result;
	}

	public static synchronized PhonexProperties getInstance() {
		if(instance == null) {
			instance = new PhonexProperties();
		}

		return instance;
	}

	public static synchronized PhonexProperties getInstance(Properties properties) {
		if(instance == null) {
			instance = new PhonexProperties(properties);
		}

		return instance;
	}
}
