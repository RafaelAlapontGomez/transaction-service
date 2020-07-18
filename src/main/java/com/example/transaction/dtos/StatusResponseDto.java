package com.example.transaction.dtos;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("Model Status Response")
@SuppressWarnings("serial")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusResponseDto implements Serializable {
	private String reference;
	private String status;
	private Double amount;
	private Double fee;
}
