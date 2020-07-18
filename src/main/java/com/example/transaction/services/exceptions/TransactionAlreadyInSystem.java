package com.example.transaction.services.exceptions;

@SuppressWarnings("serial")
public class TransactionAlreadyInSystem extends Throwable {
	
	public TransactionAlreadyInSystem(String errorMessage) {
		super(errorMessage);
	}

}
