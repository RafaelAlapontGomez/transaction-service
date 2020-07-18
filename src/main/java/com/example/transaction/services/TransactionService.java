package com.example.transaction.services;

import java.util.List;

import com.example.transaction.dtos.StatusRequestDto;
import com.example.transaction.dtos.StatusResponseDto;
import com.example.transaction.dtos.TransactionDto;
import com.example.transaction.enums.OrderType;
import com.example.transaction.services.exceptions.NoAccountPresentForThisTransaction;
import com.example.transaction.services.exceptions.NoBalaceForThisTransaction;
import com.example.transaction.services.exceptions.NoRuleForThisCase;
import com.example.transaction.services.exceptions.TransactionAlreadyInSystem;

public interface TransactionService {
	List<TransactionDto> getTransactions(String Iban, OrderType Order);
	TransactionDto createTransaction(TransactionDto transactionDto) throws NoAccountPresentForThisTransaction, NoBalaceForThisTransaction, TransactionAlreadyInSystem;
	StatusResponseDto getStatus(StatusRequestDto request) throws NoRuleForThisCase;
	TransactionDto getTransactionByReference(String reference);
}
