package com.hulkhiretech.payments.dao.impl;

import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TransactionDaoImpl implements TransactionDao {

	private final ModelMapper modelMapper;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public TransactionDaoImpl(ModelMapper modelMapper, NamedParameterJdbcTemplate jdbcTemplate) {
		this.modelMapper = modelMapper;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public TransactionDTO createTransaction(TransactionDTO txnDto) {

		log.info("Creating transaction in DB || txnDto : "+txnDto);

		TransactionEntity txnEntity = modelMapper.map(txnDto, TransactionEntity.class);
		log.info("Converted to entity txnEntity : "+txnEntity);

		String sql = "INSERT INTO payments.`Transaction` (userId, paymentMethodId, providerId, paymentTypeId, txnStatusId, " + 
				"amount, currency, merchantTxnReference, txnReference, providerReference, errorCode, errorMessage, retryCount) " + 
				"VALUES (:userId, :paymentMethodId, :providerId, :paymentTypeId, :txnStatusId, :amount, :currency, " + 
				":merchantTxnReference, :txnReference, :providerReference, :errorCode, :errorMessage, :retryCount)"; 

		SqlParameterSource params = new BeanPropertySqlParameterSource(txnEntity);
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

		int id = keyHolder.getKey()!=null ? keyHolder.getKey().intValue():-1;
		txnDto.setId(id);

		log.info("Transaction created in DB || txn id : {} | txnReference : {} ",
				txnDto.getId(),
				txnDto.getTxnReference());

		return txnDto;
	}

	@Override
	public TransactionDTO getTxnByRef(String txnReference) {

		String sql = "SELECT * FROM payments.`Transaction` "
				+ "WHERE txnReference = :txnReference";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("txnReference", txnReference);

		try {

			TransactionEntity transaction = jdbcTemplate.queryForObject(sql, params, 
					new BeanPropertyRowMapper<>(TransactionEntity.class));
			TransactionDTO txnDto = modelMapper.map(transaction, TransactionDTO.class);
			log.info("Transaction fetched from DB || txnDto : "+txnDto);
			return txnDto;

		}catch(Exception e) {

			log.error("Error while fetching transaction from DB || txnReference : "+txnReference, e);
			return null;	
		}
	}

	@Override
	public TransactionDTO updateTxnStatusDetails(TransactionDTO txnDto) {

		String sql = "UPDATE payments.`Transaction` "
				+ "SET txnStatusId = :txnStatusId, providerReference = :providerReference, "
				+ "errorCode = :errorCode, errorMessage = :errorMessage "
				+ "WHERE txnReference = :txnReference";

		TransactionEntity transaction = modelMapper.map(txnDto, TransactionEntity.class);
		SqlParameterSource params = new BeanPropertySqlParameterSource(transaction);

		jdbcTemplate.update(sql, params);
		log.info("Transaction status updated in DB || txnDto:{}",txnDto);

		return txnDto;
	}

	@Override
	public TransactionDTO getTransactionByProviderReference(String providerReference) {

		String sql = "SELECT * FROM payments.`Transaction` "
				+ "WHERE providerReference = :providerReference";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("providerReference", providerReference);

		try {

			TransactionEntity transaction = jdbcTemplate.queryForObject(sql, params, 
					new BeanPropertyRowMapper<>(TransactionEntity.class));
			
			TransactionDTO txnDto = modelMapper.map(transaction, TransactionDTO.class);
			log.info("Transaction fetched from DB || txnDto : "+txnDto);
			return txnDto;	

		}catch(Exception e) {

			log.error("Error while fetching transaction from DB || providerReference : "+providerReference, e);
			return null;	
		}
	}
}
