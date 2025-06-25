package com.hulkhiretech.payments.dto.stripe;

import com.hulkhiretech.payments.pojo.stripe.StripeData;
import lombok.Data;

@Data
public class StripeEventDTO {

	private String id;
	private String type;
	private StripeData data;
}
