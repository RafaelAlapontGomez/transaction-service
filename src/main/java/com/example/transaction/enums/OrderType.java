package com.example.transaction.enums;

import lombok.Getter;

@Getter
public enum OrderType {
	
	ASC("ASC"),
	DESC("DESC");
	
	private String orderType;
	
	OrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public static OrderType getOrderType(String order) {
		OrderType[] orders = OrderType.values();
		for(OrderType orderType: orders) {
			if (orderType.getOrderType().equals(order)) {
				return orderType;
			}
		}
		return null;
	}
	
	public static String getOrder(OrderType type) {
		OrderType[] orders = OrderType.values();
		for(OrderType orderType: orders) {
			if (orderType.equals(type)) {
				return orderType.getOrderType();
			}
		}
		return null;
		
	}
}
