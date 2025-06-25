package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class CreatePaymentRequest {

	private int userId; 

	private String paymentMethod; 
	private String provider; 
	private String paymentType; 

	private double amount;  
	private String currency; 
	private String merchantTxnReference; 
}
