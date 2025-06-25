package com.hulkhiretech.payments.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.hulkhiretech.payments.constant.TransactionStatusEnum;
import com.hulkhiretech.payments.service.impl.statushandler.CreatedStatusHandler;
import com.hulkhiretech.payments.service.impl.statushandler.FailedStatusHandler;
import com.hulkhiretech.payments.service.impl.statushandler.InitiatedStatusHandler;
import com.hulkhiretech.payments.service.impl.statushandler.PendingStatusHandler;
import com.hulkhiretech.payments.service.impl.statushandler.SuccessStatusHandler;
import com.hulkhiretech.payments.service.interfaces.TxnStatusHandler;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentStatusFactory {

	private ApplicationContext context;

	public PaymentStatusFactory(ApplicationContext context) {	
		this.context = context;
	}

	public TxnStatusHandler getHandler(TransactionStatusEnum statusEnum) {

		switch(statusEnum) {

		case CREATED:
			return context.getBean(CreatedStatusHandler.class);

		case INITIATED:
			return context.getBean(InitiatedStatusHandler.class);

		case PENDING:
			return context.getBean(PendingStatusHandler.class);

		case SUCCESS:
			return context.getBean(SuccessStatusHandler.class);

		case FAILED:
			return context.getBean(FailedStatusHandler.class);
		}
		log.error("No handler found for status || statusEnum : ", statusEnum);
		return null;
	}
}
