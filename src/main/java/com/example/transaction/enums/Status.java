package com.example.transaction.enums;

import lombok.Getter;

@Getter
public enum Status {
	
	PENDING("PENDING"),
	SETTLED("SETTLED"),
	FUTURE("FUTURE"),
	INVALID("INVALID");
	
	private String status;
	
	Status(String status) {
		this.status = status;
	}
	
	public static Status getStatus(String statu) {
		Status[] status = Status.values();
		for(Status item: status) {
			if (item.getStatus().equals(statu)) {
				return item;
			}
		}
		return null;
	}
	
	public static String getStatusStr(Status statu) {
		Status[] status = Status.values();
		for(Status item: status) {
			if (item.equals(statu)) {
				return item.getStatus();
			}
		}
		return null;
		
	}	


}
