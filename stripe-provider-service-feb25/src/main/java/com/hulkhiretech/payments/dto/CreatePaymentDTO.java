package com.hulkhiretech.payments.dto;

import java.util.List;

import com.hulkhiretech.payments.pojo.LineItem;

import lombok.Data;

@Data
public class CreatePaymentDTO {
	
	private String successUrl;
	private String cancelUrl;
	
	private List<LineItem> lineItems;

}
