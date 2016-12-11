package ru.vkulakov.phonex;

/**
 * <h3>Обёртка для класса параметров приложения для удобного тестирования</h3>
 */
public class PhonexPropertiesWrap extends PhonexProperties {
	/**
	 * <p>Приводит синглтон в исходное состояние.</p>
	 */
	public static void recycle() {
		instance = null;
	}
}
