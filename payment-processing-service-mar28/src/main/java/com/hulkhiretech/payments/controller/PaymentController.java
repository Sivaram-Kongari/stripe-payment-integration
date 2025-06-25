package com.hulkhiretech.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.CreatePaymentRequest;
import com.hulkhiretech.payments.pojo.CreatePaymentResponse;
import com.hulkhiretech.payments.pojo.InitPaymentResponse;
import com.hulkhiretech.payments.pojo.InitiatePaymentReq;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {

	private ModelMapper modelMapper;
	private PaymentService paymentService;

	public PaymentController(ModelMapper modelMapper, PaymentService paymentService) {

		this.modelMapper = modelMapper;
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest createPaymentRequest) {

		log.info("Payment created successfully : "+"createPaymentRequest : "+createPaymentRequest);

		TransactionDTO txnDto = modelMapper.map(createPaymentRequest, TransactionDTO.class);
		log.info("Transaction created successfully : txnDto : "+txnDto);

		TransactionDTO response = paymentService.createPayment(txnDto);

		CreatePaymentResponse createPaymentResponse = new CreatePaymentResponse();
		createPaymentResponse.setTxnReference(response.getTxnReference());
		createPaymentResponse.setTxnStatus(response.getTxnStatus());

		log.info("Returning createPaymentResponse : {}", createPaymentResponse);

		return new ResponseEntity<>(createPaymentResponse, HttpStatus.CREATED);
	}

	@PostMapping("/{txnReference}/initiate")
	public ResponseEntity<InitPaymentResponse> initiatePayment(@PathVariable String txnReference, 
			@RequestBody InitiatePaymentReq initiatePaymentReq) {

		log.info("Payment status fetched successfully || txnReference : {} | initiatePaymentReq : {}",
				txnReference, initiatePaymentReq);

		InitiatePaymentDTO reqDto = modelMapper.map(initiatePaymentReq, InitiatePaymentDTO.class);
		TransactionDTO responseDTO = paymentService.initiatePayment(txnReference, reqDto);

		InitPaymentResponse response = InitPaymentResponse.builder()
				.txnReference(responseDTO.getTxnReference())
				.txnStatus(responseDTO.getTxnStatus())
				.url(responseDTO.getUrl()) 
				.build();
		log.info("Returning response : {}", response);

		return ResponseEntity.ok(response);
	}

}
