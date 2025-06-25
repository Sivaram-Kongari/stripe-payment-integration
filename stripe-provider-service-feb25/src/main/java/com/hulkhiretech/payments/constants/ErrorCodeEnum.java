package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	
	GENERIC_ERROR("30000", "Unable to process the request, please try again later"),
	STRIPE_PSP_ERROR("30001", "<StripePSP error occurred>"),
	UNABLE_TO_CONNECT_TO_STRIPE_PSP("30002", "Unable to connect to StripePSP");
	
	private String errorCode;
	private String errorMessage;
	
	private ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
