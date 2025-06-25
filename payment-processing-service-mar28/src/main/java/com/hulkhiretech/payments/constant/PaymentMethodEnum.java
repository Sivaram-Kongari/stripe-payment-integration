package com.hulkhiretech.payments.constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum PaymentMethodEnum {

	APM(1, "APM");

	private int id;
	private String name;

	private PaymentMethodEnum(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public static PaymentMethodEnum getByName(String name) {

		for(PaymentMethodEnum status : values()) {
			if(status.name.equalsIgnoreCase(name)) {
				return status;
			}
		}
		log.error("No enum constant found for name : "+name);
		return null;
	}

	public static PaymentMethodEnum getById(int id) {

		for(PaymentMethodEnum status : values()) {
			if(status.id == id) {
				return status;
			}
		}
		log.error("No enum constant found for id : "+id);
		return null;
	}

}
