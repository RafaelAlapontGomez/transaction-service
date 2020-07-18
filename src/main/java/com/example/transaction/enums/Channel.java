package com.example.transaction.enums;

import lombok.Getter;

@Getter
public enum Channel {

	CLIENT("CLIENT"),
	ATM("ATM"),
	INTERNAL("INTERNAL"); 
	
	private String channel;
	
	Channel(String channel) {
		this.channel = channel;
	}
	
	public static Channel getChannel(String channel) {
		Channel[] channels = Channel.values();
		for(Channel item: channels) {
			if (item.getChannel().equals(channel)) {
				return item;
			}
		}
		return null;
	}
	
	public static String getChannelStr(Channel channelEnun) {
		Channel[] channels = Channel.values();
		for(Channel item: channels) {
			if (item.equals(channelEnun)) {
				return item.getChannel();
			}
		}
		return null;
	}	

}
