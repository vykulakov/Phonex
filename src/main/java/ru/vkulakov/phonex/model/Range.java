package ru.vkulakov.phonex.model;

/**
 * Диапазон номеров телефонов.
 */
public class Range {
	private int code;
	private int start;
	private int finish;
	private int capacity;
	private String operator;
	private String region;

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getFinish() {
		return finish;
	}
	public void setFinish(int finish) {
		this.finish = finish;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "Range {" +
				"code=" + code +
				", start=" + start +
				", finish=" + finish +
				", capacity=" + capacity +
				", operator='" + operator + '\'' +
				", region='" + region + '\'' +
				'}';
	}
}
