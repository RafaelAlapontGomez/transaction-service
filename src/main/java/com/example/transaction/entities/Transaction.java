package com.example.transaction.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Transaction implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique=true)
	private String reference;
	private String accountIban;
	private Date date;
	private Double amount;
	private Double fee;
	private String description;
}
