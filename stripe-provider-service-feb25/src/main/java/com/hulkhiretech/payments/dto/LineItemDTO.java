package com.hulkhiretech.payments.dto;

import lombok.Data;

@Data
public class LineItemDTO {

	private int quantity;
	private String currency;
	private String productName;
	private int unitAmount;
}
