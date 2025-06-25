package com.hulkhiretech.payments.dto;

import java.util.List;
import com.hulkhiretech.payments.pojo.Item;
import lombok.Data;

@Data
public class InitiatePaymentDTO {

	private String successUrl;
	private String cancelUrl;

	private List<Item> lineItems;

}
