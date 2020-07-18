package com.example.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.transaction.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByAccountIban(String accountIban);
}
