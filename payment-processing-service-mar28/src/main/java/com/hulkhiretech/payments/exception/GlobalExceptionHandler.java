package com.hulkhiretech.payments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.pojo.ErrorRes;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

	@ExceptionHandler(ProcessingException.class)
	public ResponseEntity<ErrorRes> handleStripeProviderException(ProcessingException ex) {

		log.error("StripeProviderException occured : ", ex);

		ErrorRes errorRes = new ErrorRes();
		errorRes.setErrorCode(ex.getErrorCode());
		errorRes.setErrorMessage(ex.getErrorMessage());

		log.info("Returning errorRes : "+errorRes);
		return new ResponseEntity<>(errorRes, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleGenericException(Exception ex) {

		log.error("handleGenericException occured : ", ex);

		ErrorRes errorRes = new ErrorRes();
		errorRes.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
		errorRes.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());

		log.info("Returning errorRes : "+errorRes);

		return new ResponseEntity<>(errorRes, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
