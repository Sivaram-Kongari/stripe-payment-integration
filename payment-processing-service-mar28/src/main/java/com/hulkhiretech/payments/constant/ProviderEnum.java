package com.hulkhiretech.payments.constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum ProviderEnum {

	STRIPE(1, "STRIPE");

	private int id;
	private String name;

	private ProviderEnum(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public static ProviderEnum getByName(String name) {

		for(ProviderEnum status : values()) {

			if(status.name.equalsIgnoreCase(name)) {

				return status;
			}
		}
		log.error("No enum constant found for name : "+name);
		return null;
	}

	public static ProviderEnum getById(int id) {

		for(ProviderEnum status : values()) {

			if(status.id == id) {

				return status;
			}
		}
		log.error("No enum constant found for id : "+id);
		return null;
	}
}
