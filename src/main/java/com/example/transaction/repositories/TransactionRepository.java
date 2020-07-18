package com.example.transaction.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.transaction.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByAccountIbanOrderByAmountDesc(String accountIban);
	List<Transaction> findByAccountIbanOrderByAmountAsc(String accountIban);
	Transaction findByReference(String reference);
	
	@Query(value = "SELECT reference_seq.nextval FROM dual", nativeQuery = true)
	Long getNextReference();
}
