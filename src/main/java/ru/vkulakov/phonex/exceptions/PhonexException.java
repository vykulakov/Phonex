package ru.vkulakov.phonex.exceptions;

/**
 * Класс для всех исключений внутри приложения.
 * Класс представляет непроверяемое исключение, которые не обязательно
 * для перехвата или указания в определении метода.
 */
public class PhonexException extends RuntimeException {
	public PhonexException(String message) {
		super(message);
	}

	public PhonexException(String message, Throwable cause) {
		super(message, cause);
	}
}
