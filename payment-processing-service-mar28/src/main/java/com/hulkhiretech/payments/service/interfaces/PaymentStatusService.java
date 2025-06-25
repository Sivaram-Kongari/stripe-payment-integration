package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentStatusService {

	public TransactionDTO processStatus(TransactionDTO txnDto);

}
