package com.hulkhiretech.payments.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.google.gson.Gson;
import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.dto.CreatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentDTO;
import com.hulkhiretech.payments.dto.stripe.StripeError;
import com.hulkhiretech.payments.dto.stripe.StripeErrorWrapper;
import com.hulkhiretech.payments.exception.StripeProviderException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.pojo.LineItem;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {


	private HttpServiceEngine httpServiceEngine;
	private Gson gson;

	@Value("${stripe.apiKey}")
	private String apiKey;

	@Value("${stripe.create-session.url}")
	private String createSessionUrl;

	@Value("${stripe.get-session.url}")
	private String getSessionUrl;

	public PaymentServiceImpl(HttpServiceEngine httpServiceEngine, Gson gson) {

		this.httpServiceEngine = httpServiceEngine;
		this.gson = gson;
	}

	@Override
	public PaymentDTO createPayment(CreatePaymentDTO createPaymentDTO) {

		log.info("invoked createPayment || createPaymentDTO : "+createPaymentDTO);




		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(apiKey, Constants.EMPTY_STRING);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		// Prepare form-urlencoded request body

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

		requestBody.add(Constants.MODE, Constants.MODE_PAYMENT);

		requestBody.add(Constants.SUCCESS_URL, createPaymentDTO.getSuccessUrl());
		requestBody.add(Constants.CANCEL_URL, createPaymentDTO.getCancelUrl());

		for(int i = 0; i < createPaymentDTO.getLineItems().size(); i++) {

			LineItem lineItem = createPaymentDTO.getLineItems().get(i);

			requestBody.add(String.format(Constants.LINE_ITEMS_QUANTITY, i),
					String.valueOf(lineItem.getQuantity()));

			requestBody.add(String.format(Constants.LINE_ITEMS_CURRENCY, i),
					lineItem.getCurrency());

			requestBody.add(String.format(Constants.LINE_ITEMS_PRODUCT_NAME, i),
					lineItem.getProductName());

			requestBody.add(String.format(Constants.LINE_ITEMS_UNIT_AMOUNT, i),
					String.valueOf(lineItem.getUnitAmount()));
		}

		HttpRequest httpRequest = HttpRequest.builder()
				.method(HttpMethod.POST)
				.url(createSessionUrl)
				.headers(httpHeaders)
				.requestBody(requestBody)
				.build();


		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("Response from httpServiceEngine : "+response);

		PaymentDTO paymentDTO = gson.fromJson(response.getBody(), PaymentDTO.class);
		log.info("Converted to DTO paymentDTO : "+paymentDTO);

		return paymentDTO;


	}

	@Override
	public PaymentDTO getPayment(String id) {

		log.info("invoked getPayment || id : "+id);

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.setBasicAuth(apiKey, Constants.EMPTY_STRING);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		HttpRequest httpRequest = HttpRequest.builder()
				.method(HttpMethod.GET)
				.url(getSessionUrl.replace(Constants.SESSION_ID, id))
				.headers(httpHeaders)
				.requestBody(Constants.EMPTY_STRING)
				.build();

		log.info("Passing httpRequest : "+httpRequest);

		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("Response from httpServiceEngine : "+response);

		PaymentDTO paymentDTO = processResponse(response);

		log.info("Returning paymentDTO : "+paymentDTO);

		return paymentDTO;
	}

	private PaymentDTO processResponse(ResponseEntity<String> response) {

		if(response.getStatusCode().is2xxSuccessful()) { 

			log.info("Success response received. response : "+response);
			PaymentDTO paymentDto = gson.fromJson(response.getBody(), PaymentDTO.class);

			if(paymentDto != null && paymentDto.getUrl() != null) {

				log.info("Got success url from stripe. returning paymentDTO : "+paymentDto);
				return paymentDto;	
			}
			log.error("GOT 2xx but no url in response");
		}

		StripeErrorWrapper errorObj = gson.fromJson(response.getBody(), StripeErrorWrapper.class);
		log.error("Error response received. errorObj : "+errorObj);

		if(errorObj != null && errorObj.getError() != null) {
			log.error("Error response received. errorObj : "+errorObj);

			throw new StripeProviderException(
					ErrorCodeEnum.STRIPE_PSP_ERROR.getErrorCode(),
					prepareErrorMessage(errorObj.getError()),
					HttpStatus.valueOf(response.getStatusCode().value()));
		}

		log.error("Raising generic error. Unable to get valid error object structure");

		throw new StripeProviderException(
				ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
				ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String prepareErrorMessage(StripeError stripeError) {

		return stripeError.getType()
				+ ":" + stripeError.getCode()
				+ ":" + stripeError.getParam()
				+ ":" + stripeError.getMessage();
	}

	@Override
	public PaymentDTO expiryPayment(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}