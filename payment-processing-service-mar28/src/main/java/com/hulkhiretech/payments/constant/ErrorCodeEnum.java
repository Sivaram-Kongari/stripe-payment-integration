package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {	

	GENERIC_ERROR("20000", "Unable to process the request, please try again later"),
	UNABLE_TO_CONNECT_TO_STRIPE_PS("20001", "Unable to connect to stripe-provider"),
	ERROR_AT_STRIPE_PSP("20002", "Failed process at StripePSP. please try again later."),
	INVALID_TXN_REFERENCE("20003", "Invalid txnReference. No transaction found"),
	INVALID_PAYMENT_STATUS("20004", "Invalid payment status. No Configuration Found"),
	NO_STATUS_HANDLE_FOUND("20005", "Transaction status handler not found");

	private String errorCode;
	private String errorMessage;

	private ErrorCodeEnum(String errorCode, String errorMessage) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}