package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentService {

	public TransactionDTO createPayment(TransactionDTO transactionDTO);

	//public String initiatePayment();

	public TransactionDTO initiatePayment(String txnReference, InitiatePaymentDTO reqDto);
}

