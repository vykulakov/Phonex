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

package ru.vkulakov.phonex.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Информация о номере телефона.
 */
@XmlRootElement
public class Phone {
	private int code;
	private int start;
	private int finish;
	private String phone;
	private String region;
	private String operator;

	@XmlTransient
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	@XmlTransient
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	@XmlTransient
	public int getFinish() {
		return finish;
	}
	public void setFinish(int finish) {
		this.finish = finish;
	}
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
	public Phone clone() {
		Phone phone = new Phone();
		phone.setCode(this.code);
		phone.setStart(this.start);
		phone.setFinish(this.finish);
		phone.setPhone(this.phone);
		phone.setRegion(this.region);
		phone.setOperator(this.operator);

		return phone;
	}

	@Override
	public String toString() {
		return "Phone {" +
				"code=" + code +
				", start=" + start +
				", finish=" + finish +
				", phone='" + phone + '\'' +
				", region='" + region + '\'' +
				", operator='" + operator + '\'' +
				'}';
	}
}
