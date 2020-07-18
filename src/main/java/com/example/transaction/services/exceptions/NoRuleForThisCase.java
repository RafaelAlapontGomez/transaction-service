package com.example.transaction.services.exceptions;

@SuppressWarnings("serial")
public class NoRuleForThisCase extends Exception {
	public NoRuleForThisCase(String errorMessage) {
		super(errorMessage);
	}

}
