package com.hulkhiretech.payments.pojo.stripe;

import lombok.Data;

@Data	
public class StripeEvent {

	private String id;
	private String type;	
	private StripeData data;
	
}
