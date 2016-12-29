package ru.vkulakov.phonex.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Результат поиска информации по номеру телефона.
 */
@XmlRootElement
public class Result {
	private int result;
	private String message;
	private Phone phone;

	/**
	 * Значение не передано или передано пустое значение.
	 */
	public final static int EMPTY = 1;

	/**
	 * Значение имеет неправильный формат.
	 */
	public final static int BAD_FORMAT = 2;

	/**
	 * Результат по переданному значению не найден.
	 */
	public final static int NOT_FOUND = 3;

	/**
	 * Слишком короткое значение.
	 */
	public final static int TOO_SHORT = 4;

	/**
	 * Найдено слишком много результатов по переданному значению.
	 */
	public final static int TOO_MANY = 5;

	/**
	 * Внутрення ошибка.
	 */
	public final static int ERROR = 99;

	public Result() {
	}
	public Result(int result, String message) {
		this.result = result;
		this.message = message;
	}
	public Result(Phone phone) {
		this.result = 0;
		this.message = "";
		this.phone = phone;
	}

	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Result {" +
				"result=" + result +
				", message='" + message + '\'' +
				", phone=" + phone +
				'}';
	}
}
