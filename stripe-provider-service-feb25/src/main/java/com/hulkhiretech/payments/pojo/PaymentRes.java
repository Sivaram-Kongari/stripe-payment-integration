package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class PaymentRes {

	private String id;
	private String url;
	private String status;
	private String payment_status;
}
