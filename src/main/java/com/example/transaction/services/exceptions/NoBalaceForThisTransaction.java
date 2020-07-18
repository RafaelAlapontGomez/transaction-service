package com.example.transaction.services.exceptions;

@SuppressWarnings("serial")
public class NoBalaceForThisTransaction extends Exception {

	public NoBalaceForThisTransaction(String errorMessage) {
		super(errorMessage);
	}

}
