package com.example.transaction.dtos;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("Model Transaction")
@SuppressWarnings("serial")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionDto implements Serializable {
	
	private String reference;
	@NotNull
	private String accountIban;
	private Date date;
	@NotNull
	private Double amount;
	private Double fee;
	private String description;
}
