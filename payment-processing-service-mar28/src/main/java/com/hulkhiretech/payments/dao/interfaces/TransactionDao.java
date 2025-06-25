package com.hulkhiretech.payments.dao.interfaces;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface TransactionDao {

	public TransactionDTO createTransaction(TransactionDTO txnDto);
	public TransactionDTO getTxnByRef(String txnReference);
	public TransactionDTO updateTxnStatusDetails(TransactionDTO txnDto);
	
	public TransactionDTO getTransactionByProviderReference(String providerReference);
}
