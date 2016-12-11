package ru.vkulakov.phonex.exceptions;

/**
 * <h3>Класс для всех исключений внутри приложения</h3>
 * <p>Класс представляет непроверяемое исключение, которые не обязательно
 * для перехвата или указания в определении метода.</p>
 * @author Кулаков Вячеслав <kulakov.home@gmail.com>
 */
public class PhonexException extends RuntimeException {
	public PhonexException(String message) {
		super(message);
	}

	public PhonexException(String message, Throwable cause) {
		super(message, cause);
	}
}
