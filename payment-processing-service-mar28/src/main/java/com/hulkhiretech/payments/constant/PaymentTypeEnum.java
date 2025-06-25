package com.hulkhiretech.payments.constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum PaymentTypeEnum {

	SALE(1, "SALE");

	private int id;
	private String name;

	private PaymentTypeEnum(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public static PaymentTypeEnum getByName(String name) {

		for(PaymentTypeEnum status : values()) {

			if(status.name.equalsIgnoreCase(name)) {

				return status;
			}
		}
		log.error("No enum constant found for name : "+name);
		return null;
	}

	public static PaymentTypeEnum getById(int id) {

		for(PaymentTypeEnum status : values()) {

			if(status.id == id) {

				return status;
			}
		}
		log.error("No enum constant found for id : "+id);
		return null;
	}
}
