package com.hulkhiretech.payments.service.impl.statushandler;

import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.service.interfaces.TxnStatusHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreatedStatusHandler implements TxnStatusHandler {

	private TransactionDao transactionDao;

	public CreatedStatusHandler(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

	@Override
	public TransactionDTO processStatus(TransactionDTO txnDto) {

		log.info("Processing CREATED status || txnDto : "+txnDto);
		txnDto = transactionDao.createTransaction(txnDto);
		log.info("Created Txn in DB || txnDto : "+txnDto);

		return txnDto;
	}

}