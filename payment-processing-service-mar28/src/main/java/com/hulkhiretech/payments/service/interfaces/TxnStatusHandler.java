package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.TransactionDTO;


public interface TxnStatusHandler {
	
	public TransactionDTO processStatus(TransactionDTO txnDto);
}