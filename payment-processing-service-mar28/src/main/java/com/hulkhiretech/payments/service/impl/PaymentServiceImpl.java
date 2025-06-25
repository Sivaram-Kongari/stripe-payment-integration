package com.hulkhiretech.payments.service.impl;

import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.hulkhiretech.payments.constant.Constants;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.constant.TransactionStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentResDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.exception.ProcessingException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import com.hulkhiretech.payments.service.interfaces.PaymentStatusService;
import com.hulkhiretech.payments.stripeprovider.CreatePaymentReq;
import com.hulkhiretech.payments.stripeprovider.PaymentRes;
import com.hulkhiretech.payments.stripeprovider.SPErrorResponse;
import com.hulkhiretech.payments.util.GsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentStatusService paymentStatusService;
	private final HttpServiceEngine httpServiceEngine;
	private final Gson gson;
	private final TransactionDao transactionDao;
	private final ModelMapper modelMapper;
	private final GsonUtils gsonUtils;


	@Value("${stripe.provider.create.payment.url}")
	private String stripeProviderCreatePaymentUrl;

	@Override
	public TransactionDTO createPayment(TransactionDTO transactionDTO) {

		log.info("Received transactionDTO : "+transactionDTO);

		transactionDTO.setTxnStatus(TransactionStatusEnum.CREATED.getName());
		transactionDTO.setTxnReference(generatedTypeReference());

		transactionDTO = paymentStatusService.processStatus(transactionDTO); 
		log.info("Created payment in DB : transactionDTO :"+transactionDTO);

		return transactionDTO;
	}

	private String generatedTypeReference() {

		String txnReference = UUID.randomUUID().toString();
		log.info("Generated txnReference : "+txnReference);

		return txnReference;
	}

	@Override
	public TransactionDTO initiatePayment(String txnReference, InitiatePaymentDTO reqDto) {

		log.info("Initiating payment txnReference : {} | reqDto : {}", txnReference, reqDto);

		TransactionDTO txnDto = transactionDao.getTxnByRef(txnReference); 
		log.info("txnDto from DB : "+txnDto);

		if(txnDto == null) {

			throw new ProcessingException(
					ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorCode(), 
					ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorMessage(),
					HttpStatus.BAD_REQUEST);
		}

		txnDto.setTxnStatus(TransactionStatusEnum.INITIATED.getName());
		paymentStatusService.processStatus(txnDto);

		HttpRequest httpRequest = prepareHttpRequest(reqDto);
		log.info("Prepared httpRequest : "+httpRequest);

		try {

			ResponseEntity<String> httResponse = httpServiceEngine.makeHttpCall(httpRequest);
			PaymentResDTO paymentResponse = processResponse(httResponse);

			txnDto.setTxnStatus(TransactionStatusEnum.PENDING.getName());
			txnDto.setProviderReference(paymentResponse.getId());
			txnDto.setUrl(paymentResponse.getUrl());
			paymentStatusService.processStatus(txnDto);

			log.info("Successfully got url & update in DB : "+txnDto);

			return txnDto;


		}catch(ProcessingException e) {

			txnDto.setTxnStatus(TransactionStatusEnum.FAILED.getName());
			txnDto.setErrorCode(e.getErrorCode());
			txnDto.setErrorMessage(e.getErrorMessage());
			paymentStatusService.processStatus(txnDto);

			if(e.getErrorCode().equals(Constants.STRIPE_PSP_ERROR)) {

				log.error("Got error with stripe PSP, so returning standard error to invoke");
				throw new ProcessingException(
						ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorCode(),
						ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorMessage(),
						e.getHttpStatus());
			}

			throw e;
		}
	}

	private PaymentResDTO processResponse(ResponseEntity<String> httResponse) {

		if(httResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED)) {

			PaymentRes spPaymentRes = gsonUtils.fromJson(httResponse.getBody(), PaymentRes.class);
			log.info("Converted to paymentRes : "+spPaymentRes);

			if(spPaymentRes!=null && spPaymentRes.getUrl()!=null) {

				PaymentResDTO paymentResDTO = modelMapper.map(spPaymentRes, PaymentResDTO.class);
				log.info("Converted paymentRes to PaymentResDTO : "+paymentResDTO);
				return paymentResDTO;
			}
			log.error("GOT 201 but no url in response");
		}
		SPErrorResponse errorResponse = gsonUtils.fromJson(httResponse.getBody(), SPErrorResponse.class);
		log.error("Converted to SPErrorResponse : "+errorResponse);

		if(errorResponse!=null && errorResponse.getErrorCode()!=null) {

			throw new ProcessingException(
					errorResponse.getErrorCode(), 
					errorResponse.getErrorMessage(),
					HttpStatus.valueOf(httResponse.getStatusCode().value()));
		}

		return null;
	}

	private HttpRequest prepareHttpRequest(InitiatePaymentDTO reqDto) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		CreatePaymentReq paymentReq = modelMapper.map(reqDto, CreatePaymentReq.class);

		HttpRequest httpRequest = HttpRequest.builder()
				.method(HttpMethod.POST)
				.url(stripeProviderCreatePaymentUrl)
				.headers(httpHeaders)
				.requestBody(gson.toJson(paymentReq))
				.build();

		log.info("Prepare httpRequest : "+httpRequest);

		return httpRequest;
	}

	public GsonUtils getGsonUtils() {
		return gsonUtils;
	}

}