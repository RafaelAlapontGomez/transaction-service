package com.example.transaction.services.exceptions;

@SuppressWarnings("serial")
public class NoAccountPresentForThisTransaction extends Exception {

	public NoAccountPresentForThisTransaction(String errorMessage) {
		super(errorMessage);
	}

}
