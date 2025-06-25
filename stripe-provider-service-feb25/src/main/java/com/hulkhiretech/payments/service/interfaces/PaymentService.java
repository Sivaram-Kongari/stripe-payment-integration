package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.CreatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentDTO;

public interface PaymentService {

	public PaymentDTO createPayment(CreatePaymentDTO paymentDTO);
	public PaymentDTO getPayment(String id);
	public PaymentDTO expiryPayment(String id);

}
