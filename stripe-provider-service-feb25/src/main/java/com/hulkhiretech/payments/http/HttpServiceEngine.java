package com.hulkhiretech.payments.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.StripeProviderException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpServiceEngine {

	private RestClient restClient;

	public HttpServiceEngine(RestClient.Builder restClientBuilder) {

		this.restClient = restClientBuilder.build();
	}

	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {

		log.info("invoked makeHttpCall || httpRequest : "+httpRequest);

		try {

			ResponseEntity<String> response = restClient.method(httpRequest.getMethod())
					.uri(httpRequest.getUrl())
					.headers(headers -> headers.addAll(httpRequest.getHeaders()))
					.body(httpRequest.getRequestBody())
					.retrieve()
					.toEntity(String.class);
			log.info("Response : "+response);

			return response;

		}catch(HttpClientErrorException | HttpServerErrorException e) {

			log.error("HttpClientErrorException occured : ", e);

			if(e.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)
					|| e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {

				log.error("received error from 5xx statusCode : "+e.getStatusCode(), e);
				throw new StripeProviderException(ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(),
						ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
						HttpStatus.valueOf(e.getStatusCode().value()));
			}

			log.info("Got getResponseBodyAsString : " + e.getResponseBodyAsString());
			return ResponseEntity.status(e.getStatusCode())
					.body(e.getResponseBodyAsString());         

		}catch(Exception e) {

			log.error("Exception occured : ", e);
			throw new StripeProviderException(
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(),
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}