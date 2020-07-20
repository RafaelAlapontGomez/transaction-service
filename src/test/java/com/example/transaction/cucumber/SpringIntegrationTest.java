package com.example.transaction.cucumber;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.transaction.TransactionServiceApplication;
import com.example.transaction.dtos.StatusRequestDto;
import com.example.transaction.dtos.StatusResponseDto;
import com.example.transaction.dtos.TransactionDto;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TransactionServiceApplication.class)
public class SpringIntegrationTest {

	private final String SERVER_URL = "http://localhost";
	private final String TRANSACTION_ENDPOINT = "/transaction/status";
	private final String SEARCH_ENDPOINT = "/transaction/search";
	private final String CREATE_TRANSACTION = "/transaction/create";
	private final String GET_TRANSACTION = "/transaction/";
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	protected int port;

	public SpringIntegrationTest(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	private String statusEndpoint() {
		return SERVER_URL + ":" + port + TRANSACTION_ENDPOINT;
	}

	private String searchEndpoint() {
		return SERVER_URL + ":" + port + SEARCH_ENDPOINT;
	}
	

	private String transactionEndpoint() {
		return SERVER_URL + ":" + port + CREATE_TRANSACTION;
	}
	
	private String searchTransactionEndpoint() {
		return SERVER_URL + ":" + port + GET_TRANSACTION;
	}
	
	public ResponseEntity<StatusResponseDto> getStatusResponse(StatusRequestDto request) {
		 final String uri = statusEndpoint();

		 UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri)
	       .queryParam("reference", request.getReference())
	       .queryParam("channel", request.getChannel());
		 
		 ResponseEntity<StatusResponseDto> result = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null,
				 new ParameterizedTypeReference<StatusResponseDto>() {});
		 return result;
	}

	public ResponseEntity<List<TransactionDto>> getTransactionResponse(String iban, String order) {
		 final String uri = searchEndpoint();

		 UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri)
	       .queryParam("iban", iban)
	       .queryParam("order", order);
		 
		 ResponseEntity<List<TransactionDto>> result = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null,
				 new ParameterizedTypeReference<List<TransactionDto>>() {});
		 return result;
	}
	

	public TransactionDto getTransactionByReference(String reference) {
		 final String uri = searchTransactionEndpoint() + reference;

		 TransactionDto result = restTemplate.getForObject(uri, TransactionDto.class);
		 return result;
	}
	
	public ResponseEntity<TransactionDto> createTransaction(TransactionDto transactionDto) {
		HttpEntity<TransactionDto> transactionEntity = new HttpEntity<>(transactionDto);
		return restTemplate.exchange(transactionEndpoint(), HttpMethod.POST, transactionEntity, TransactionDto.class);
		
	}
	

}
