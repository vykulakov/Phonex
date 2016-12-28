package ru.vkulakov.phonex.exceptions;

/**
 * Исключение при работе с параметрами приложения.
 */
public class PropertiesException extends PhonexException {
	public PropertiesException(String message) {
		super(message);
	}

	public PropertiesException(String message, Throwable cause) {
		super(message, cause);
	}
}
