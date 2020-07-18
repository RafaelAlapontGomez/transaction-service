package com.example.transaction.services.exceptions;

@SuppressWarnings("serial")
public class NeedChannelForStatus extends Throwable {
	
	public NeedChannelForStatus(String errorMessage) {
		super(errorMessage);
	}

}
