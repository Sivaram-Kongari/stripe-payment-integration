package com.hulkhiretech.payments.constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum TransactionStatusEnum {

	CREATED(1, "CREATED"),
	INITIATED(2, "INITIATED"),
	PENDING(3, "PENDING"),
	SUCCESS(4, "SUCCESS"),
	FAILED(5, "FAILED");


	private int id;
	private String name;

	private TransactionStatusEnum(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public static TransactionStatusEnum getByName(String name) {

		for(TransactionStatusEnum status : values()) {

			if(status.name.equalsIgnoreCase(name)) {

				return status;
			}
		}
		log.error("No enum constant found for name : "+name);
		return null;
	}

	public static TransactionStatusEnum getById(int id) {

		for(TransactionStatusEnum status : values()) {

			if(status.id == id) {

				return status;
			}
		}
		log.error("No enum constant found for id : "+id);
		return null;
	}
}