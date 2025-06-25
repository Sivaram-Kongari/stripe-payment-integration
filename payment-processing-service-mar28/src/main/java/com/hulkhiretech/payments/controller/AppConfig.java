package com.hulkhiretech.payments.controller;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;
import com.hulkhiretech.payments.util.PaymentMethodEnumConverter;
import com.hulkhiretech.payments.util.PaymentTypeEnumConverter;
import com.hulkhiretech.payments.util.ProviderEnumConverter;
import com.hulkhiretech.payments.util.TransactionStatusEnumConverter;
import com.hulkhiretech.payments.util.mmconverts.idtostring.PaymentMethodEnumIdToStringConverter;
import com.hulkhiretech.payments.util.mmconverts.idtostring.PaymentTypeEnumIdToStringConverter;
import com.hulkhiretech.payments.util.mmconverts.idtostring.ProviderEnumIdToStringConverter;
import com.hulkhiretech.payments.util.mmconverts.idtostring.TransactionStatusEnumIdToStringConverter;

@Configuration
public class AppConfig {

	@Bean
	ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Converter<String, Integer> paymentMethodEnumConverter = new PaymentMethodEnumConverter();
		Converter<String, Integer> providerEnumConverter = new ProviderEnumConverter();
		Converter<String, Integer> paymentTypeEnumConverter = new PaymentTypeEnumConverter();
		Converter<String, Integer> transactionStatusEnumConverter = new TransactionStatusEnumConverter();

		modelMapper.addMappings(new PropertyMap<TransactionDTO, TransactionEntity>() {

			@Override
			protected void configure() {

				using(paymentMethodEnumConverter).map(source.getPaymentMethod(), destination.getPaymentMethodId());
				using(providerEnumConverter).map(source.getProvider(), destination.getProviderId());
				using(paymentTypeEnumConverter).map(source.getPaymentType(), destination.getPaymentTypeId());
				using(transactionStatusEnumConverter).map(source.getTxnStatus(), destination.getTxnStatusId());
			}
		});

		modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDTO>() {

			@Override
			protected void configure() {

				using(new PaymentMethodEnumIdToStringConverter()).map(source.getPaymentMethodId(), destination.getPaymentMethod());
				using(new ProviderEnumIdToStringConverter()).map(source.getProviderId(), destination.getProvider());
				using(new PaymentTypeEnumIdToStringConverter()).map(source.getPaymentTypeId(), destination.getPaymentType());
				using(new TransactionStatusEnumIdToStringConverter()).map(source.getTxnStatusId(), destination.getTxnStatus());
			}
		});

		return modelMapper;
	}
}
