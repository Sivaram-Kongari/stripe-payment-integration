package com.hulkhiretech.payments.dto.stripe;

import lombok.Data;

@Data
public class StripeErrorWrapper {
	
	private StripeError error;

}
