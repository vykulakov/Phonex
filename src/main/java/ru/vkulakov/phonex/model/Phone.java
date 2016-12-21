package ru.vkulakov.phonex.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Информация о номере телефона.
 */
@XmlRootElement
public class Phone {
	private String phone;
	private String region;
	private String operator;

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "Phone{" +
				"phone='" + phone + '\'' +
				", region='" + region + '\'' +
				", operator='" + operator + '\'' +
				'}';
	}
}
