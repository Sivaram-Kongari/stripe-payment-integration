package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class CreatePaymentResponse {

	private String txnReference;
	private String txnStatus;
}
