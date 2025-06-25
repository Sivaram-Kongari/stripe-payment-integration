package com.hulkhiretech.payments.service.impl.statushandler;

import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.service.interfaces.TxnStatusHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitiatedStatusHandler implements TxnStatusHandler {

	private final TransactionDao transactionDao;

	@Override
	public TransactionDTO processStatus(TransactionDTO txnDto) {

		log.info("Processing INITIATED status || txnDto : "+txnDto);
		transactionDao.updateTxnStatusDetails(txnDto);
		log.info("Updated txn in DB || txnDto : "+txnDto);

		return txnDto;
	}

}
