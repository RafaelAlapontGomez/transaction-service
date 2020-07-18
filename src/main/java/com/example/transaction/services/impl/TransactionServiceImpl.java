package com.example.transaction.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.transaction.dtos.StatusRequestDto;
import com.example.transaction.dtos.StatusResponseDto;
import com.example.transaction.dtos.TransactionDto;
import com.example.transaction.entities.Account;
import com.example.transaction.entities.Transaction;
import com.example.transaction.enums.Channel;
import com.example.transaction.enums.OrderType;
import com.example.transaction.enums.Status;
import com.example.transaction.repositories.AccountRepository;
import com.example.transaction.repositories.TransactionRepository;
import com.example.transaction.services.TransactionService;
import com.example.transaction.services.exceptions.NoAccountPresentForThisTransaction;
import com.example.transaction.services.exceptions.NoBalaceForThisTransaction;
import com.example.transaction.services.exceptions.NoRuleForThisCase;
import com.example.transaction.services.exceptions.TransactionAlreadyInSystem;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	Mapper dozerMapper;
	
	@Autowired
	TransactionRepository repository;

	@Autowired
	AccountRepository accountRepository; 
	
	
	@Override
	public List<TransactionDto> getTransactions(String iban, OrderType order) {
		List<Transaction> transactions = null;
		Direction direction = order.getOrderType().equals("ASC") ? Sort.Direction.ASC: Sort.Direction.DESC;
		if (iban == null || iban.length() == 0) {
			Sort sort = Sort.by(direction, "amount");
			transactions = repository.findAll(sort);
		} else {
			if (direction.isAscending()) {
				transactions = repository.findByAccountIbanOrderByAmountAsc(iban);
			} else {
				transactions = repository.findByAccountIbanOrderByAmountDesc(iban);
			}
		}

		return entitiesToDtos(transactions);
	}

	@Override
	public TransactionDto createTransaction(TransactionDto transactionDto) throws NoAccountPresentForThisTransaction, NoBalaceForThisTransaction, TransactionAlreadyInSystem {
		
		if (transactionDto.getReference() != null) {
			Transaction transaction = repository.findByReference(transactionDto.getReference());
			if (transaction != null) {
				throw new TransactionAlreadyInSystem(String.format("The transaction %s already in the system", transactionDto.getReference()));
			}
		}
		Account account = accountRepository.findByAccountIban(transactionDto.getAccountIban());
		if (account == null) {
			throw new NoAccountPresentForThisTransaction("No hay cuenta donde aplicar esta transacción");
		}
		
		// if fee is present, deducted fee from amount
		Double amount = transactionDto.getAmount();
		if (transactionDto.getFee() != null) {
			amount = transactionDto.getAmount() - transactionDto.getFee();
		} 
		
		// add amount to totalBalance
		Double totalBalance = account.getTotalAccountBalance();
		if (amount >= 0) {
			account.setTotalAccountBalance(totalBalance + amount);
		} else {
			amount = -amount;
			if (totalBalance - amount < 0) {
				throw new NoBalaceForThisTransaction("No hay saldo suficiente");
			} else {
				account.setTotalAccountBalance(totalBalance - amount);
			}
		}
		
		accountRepository.save(account);
		
		if (transactionDto.getReference() == null || transactionDto.getReference().length() == 0) {
			transactionDto.setReference(createReference());
		}
		Transaction transaction = dozerMapper.map(transactionDto, Transaction.class); 
		Transaction newTransaction = repository.save(transaction);
		return dozerMapper.map(newTransaction, TransactionDto.class);
	}

	@Override
	public StatusResponseDto getStatus(StatusRequestDto request) throws NoRuleForThisCase {
		LocalDate today = LocalDate.now();
		Transaction transaction = repository.findByReference(request.getReference());
		if (transaction == null) {
			return StatusResponseDto.builder()
					.reference(request.getReference())
					.status(Status.INVALID.getStatus())
					.build();
		}
		
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		Channel channel = Channel.getChannel(request.getChannel());
		if (channel.equals(Channel.ATM) || channel.equals(Channel.CLIENT)) {
			if (today.compareTo(transactionDate) > 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.SETTLED.getStatus())
						.amount(transaction.getAmount() - transaction.getFee())
						.build();
			}

			if (today.compareTo(transactionDate) == 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.PENDING.getStatus())
						.amount(transaction.getAmount() - transaction.getFee())
						.build();
				
			}
		}
		
		if (channel.equals(Channel.INTERNAL)) {
			if (today.compareTo(transactionDate) > 0  ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.SETTLED.getStatus())
						.amount(transaction.getAmount())
						.fee(transaction.getFee())
						.build();
			}
			
			if (today.compareTo(transactionDate) < 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.FUTURE.getStatus())
						.amount(transaction.getAmount())
						.fee(transaction.getFee())
						.build();
			}

			if (today.compareTo(transactionDate) == 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.PENDING.getStatus())
						.amount(transaction.getAmount())
						.fee(transaction.getFee())
						.build();
			}
			
		}
		
		
		if (channel.equals(Channel.CLIENT)) {
			if (today.compareTo(transactionDate) < 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.FUTURE.getStatus())
						.amount(transaction.getAmount() - transaction.getFee())
						.build();
			}
		}
		
		if (channel.equals(Channel.ATM)) {
			if (today.compareTo(transactionDate) < 0 ) {
				return StatusResponseDto.builder()
						.reference(request.getReference())
						.status(Status.PENDING.getStatus())
						.amount(transaction.getAmount() - transaction.getFee())
						.build();
			}
		}
		
		throw new NoRuleForThisCase("No hay regla definida para este caso");
	}

	private String createReference() {
		Random r2 = new Random();
		
		Long serial = repository.getNextReference();
		char ascii = (char) (r2.nextInt(16) + 65);
		String reference = String.format("%05d%c", serial, ascii);
		return reference;
	}

	private List<TransactionDto> entitiesToDtos(List<Transaction> transactions) {
		List<TransactionDto> transactionsDto = new ArrayList<>();
		for(Transaction transaction : transactions) {
			TransactionDto tranDto = dozerMapper.map(transaction, TransactionDto.class); 
			transactionsDto.add(tranDto);
		}
		return transactionsDto;
	}

	@Override
	public TransactionDto getTransactionByReference(String reference) {
		TransactionDto tranDto = dozerMapper.map(repository.findByReference(reference), TransactionDto.class);
		return tranDto;
	}


}
