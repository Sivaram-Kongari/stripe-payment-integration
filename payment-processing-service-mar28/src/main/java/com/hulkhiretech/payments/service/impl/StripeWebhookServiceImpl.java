package com.hulkhiretech.payments.service.impl;

import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.hulkhiretech.payments.constant.TransactionStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.dto.stripe.CheckoutSessionCompletedData;
import com.hulkhiretech.payments.dto.stripe.StripeEventDTO;
import com.hulkhiretech.payments.service.interfaces.PaymentStatusService;
import com.hulkhiretech.payments.service.interfaces.StripeWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookServiceImpl implements StripeWebhookService {

	
	
	private static final String COMPLETE = "complete";
	private static final String PAYMENT_STATUS_PAID = "payment.status.paid";
	private static final String CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED = "checkout.session.async.payment.failed";
	private static final String CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED = "checkout.session.async.payment.succeeded";
	private static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";

	private final Gson gson;
	private final PaymentStatusService paymentStatusService;
	private final TransactionDao transactionDao;
	
	@Override
	public void processEvent(StripeEventDTO eventDTO) {
		
		if(CHECKOUT_SESSION_COMPLETED.equals(eventDTO.getType())) {
			
			CheckoutSessionCompletedData objData = 
					gson.fromJson(eventDTO.getData().getObject(), CheckoutSessionCompletedData.class);
			
			log.info("Checkout session completed event received || objData :{} ", objData);
			
			if(COMPLETE.equals(objData.getStatus())
					&& PAYMENT_STATUS_PAID.equals(objData.getPaymentStatus())) {
				
				log.info("Payment success");
				
				TransactionDTO txnDto = transactionDao.getTransactionByProviderReference(objData.getId());		
				
				if(txnDto == null) {
					
					log.error("No transaction found for reference || providerReference : {}", objData.getId());
					return;
				}
				
				log.info("Transaction found || txnDto : {} ", txnDto);
				
				txnDto.setTxnStatus(TransactionStatusEnum.SUCCESS.getName());
				paymentStatusService.processStatus(txnDto);
			}
			
			return ;
		}
		
		if(CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED.equals(eventDTO.getType())) {
			
			log.info(CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED);
			return;
		}
		
		if(CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED.equals(eventDTO.getType())) {
			
			log.info("CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED");
			return;
		}
		log.info("Event Type not configured eventType : {}", eventDTO.getType());
	}

}
