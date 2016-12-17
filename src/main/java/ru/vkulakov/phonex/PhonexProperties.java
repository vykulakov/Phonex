package ru.vkulakov.phonex;

import oracle.jrockit.jfr.StringConstantPool;
import ru.vkulakov.phonex.exceptions.PhonexException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <h3>Получение параметров приложения</h3>
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
	 * <p>Получение значения параметра по его имени.</p>
	 * <p>Если параметр не найден, то выбрасывается {@link PropertiesException}.</p>
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
	 * <p>Получение значения параметра по его имени.</p>
	 * <p>Если параметр не найден, то возвращается значение по-умолчанию.</p>
	 * @param name имя параметра.
	 * @param defValue значение по-умолчанию для параметра.
	 * @return Значение параметра или значение по-умолчанию.
	 */
	public String getProperty(String name, String defValue) {
		return properties.getProperty(name, defValue);
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

	/**
	 * <h3>Исключение при работе с параметрами приложения</h3>
	 */
	public static class PropertiesException extends PhonexException {
		public PropertiesException(String message) {
			super(message);
		}

		public PropertiesException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
