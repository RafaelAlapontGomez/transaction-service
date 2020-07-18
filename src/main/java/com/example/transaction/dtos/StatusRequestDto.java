package com.example.transaction.dtos;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("Model Status request")
@SuppressWarnings("serial")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusRequestDto implements Serializable {
	private String reference;
	private String channel;
}
