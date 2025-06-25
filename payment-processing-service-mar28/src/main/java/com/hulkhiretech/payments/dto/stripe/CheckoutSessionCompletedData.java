package com.hulkhiretech.payments.dto.stripe;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CheckoutSessionCompletedData {

	private String id;
	private String status;
	
	@SerializedName("payment_status")
	private String paymentStatus;
	
}
