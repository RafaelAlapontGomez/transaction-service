package com.example.transaction.cucumber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.transaction.dtos.StatusRequestDto;
import com.example.transaction.dtos.StatusResponseDto;
import com.example.transaction.dtos.TransactionDto;
import com.example.transaction.enums.Channel;
import com.example.transaction.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TransactionStepDefinition extends SpringIntegrationTest {

	private StatusRequestDto request;
	private ResponseEntity<StatusResponseDto> response;
	private HttpStatus status;
	TransactionDto transactionDto;
	ResponseEntity<TransactionDto> createResponse;
	ResponseEntity<List<TransactionDto>> searchResponse;
	String iban;
	String order;
	//TransactionDto transaction;
	
	public TransactionStepDefinition(RestTemplate restTemplate) {
		super(restTemplate);
	}

	@Before
	public void init() {
		request = new StatusRequestDto();
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************

	@Given("A new transaction")
	public void a_new_transaction(String transaction) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		transactionDto = mapper.readValue(transaction, TransactionDto.class);
	}
	
	@When("I invoke a method post")
	public void i_invoke_a_method_post() {
		createResponse = createTransaction(transactionDto);
	}
	
	@Then("The system store the transaction in our system")
	public void the_system_store_the_transaction_in_our_system() {
		assertThat(createResponse.getStatusCode(), is(HttpStatus.CREATED));
	}
	
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************

	@Given("A iban a order ascendente")
	public void a_iban_a_order_ascendente(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		iban = list.get(0).get("iban");
		order = list.get(0).get("order");
	}
	
	@When("I invoke a method get")
	public void i_invoke_a_method_get_search() {
		searchResponse = getTransactionResponse(iban, order);
	}
	
	@Then("The system returns list of transactions")
	public void the_system_returns_list_of_transactions() {
		assertThat(searchResponse.getStatusCode(), is(HttpStatus.OK));
		assertNotNull(searchResponse.getBody());
		assertTrue(searchResponse.getBody().size() > 0);
	}
	
	
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
 	@Given("A transaction that is not stored in our system")
	public void a_transaction_that_is_not_stored_in_our_system() {
		request.setReference("00001F");
		request.setChannel(Channel.ATM.getChannel());
	}
	
	@When("I check the status from any channel")
	public void i_check_the_status_from_any_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
		
	}
	
	@Then("The system returns the status INVALID")
	public void the_system_returns_the_status_invalid() {
		assertEquals(HttpStatus.OK, status);
		assertEquals(Status.INVALID.getStatus(), response.getBody().getStatus());
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEB A transaction that is stored in our system with this payload")
	public void caseb_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEB I check the status from CLIENT or ATM channel")
	public void caseb_i_check_the_status_from_CLIENT_or_ATM_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEB the transaction date is before today")
	public void caseb_the_transaction_date_is_before_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) > 0);
	}
	
	@Then("CASEB The system returns the above expected")
	public void caseb_the_system_returns_the_status_SETTLED(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEB the amount substracting the fee")
	public void caseb_the_amount_substracting_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		Double esperado = amount - fee;
		assertEquals(esperado, response.getBody().getAmount());
	}
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEC A transaction that is stored in our system with this payload")
	public void casec_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEC I check the status from INTERNAL channel")
	public void casec_i_check_the_status_from_INTERNAL_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEC the transaction date is before today")
	public void casec_the_transaction_date_is_before_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) > 0);
	}
	
	@Then("CASEC The system returns the above expected")
	public void casec_the_system_returns_the_status_SETTLED(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEC the amoount")
	public void casec_the_amount(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		assertEquals(amount, response.getBody().getAmount());
	}

	@And("CASEC the fee")
	public void casec_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		assertEquals(fee, response.getBody().getFee());
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASED A transaction that is stored in our system with this payload")
	public void cased_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASED I check the status from CLIENT or ATM channel")
	public void cased_i_check_the_status_from_CLIENT_or_ATM_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASED the transaction date is today")
	public void cased_the_transaction_date_is_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) == 0);
	}
	
	@Then("CASED The system returns the above expected")
	public void cased_the_system_returns_the_status_PENDING(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASED the amount substracting the fee")
	public void cased_the_amount_substracting_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		Double esperado = amount - fee;
		assertEquals(esperado, response.getBody().getAmount());
	}
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEE A transaction that is stored in our system with this payload")
	public void casee_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEE I check the status from INTERNAL channel")
	public void casee_i_check_the_status_from_INTERNAL_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEE the transaction date istoday")
	public void casee_the_transaction_date_is_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) == 0);
	}
	
	@Then("CASEE The system returns the above expected")
	public void casee_the_system_returns_the_status_INTERNAL(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEE the amoount")
	public void casee_the_amount(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		assertEquals(amount, response.getBody().getAmount());
	}

	@And("CASEE the fee")
	public void casee_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		assertEquals(fee, response.getBody().getFee());
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEF A transaction that is stored in our system with this payload")
	public void casef_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEF I check the status from CLIENT channel")
	public void casef_i_check_the_status_from_CLIENT_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEF the transaction date is after today")
	public void cased_the_transaction_date_is_after_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) < 0);
	}
	
	@Then("CASEF The system returns the above expected")
	public void casef_the_system_returns_the_status_PENDING(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEF the amount substracting the fee")
	public void casef_the_amount_substracting_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		Double esperado = amount - fee;
		assertEquals(esperado, response.getBody().getAmount());
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEG A transaction that is stored in our system with this payload")
	public void caseg_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEG I check the status from ATM channel")
	public void caseg_i_check_the_status_from_ATM_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEG the transaction date is after today")
	public void caseg_the_transaction_date_is_after_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) < 0);
	}
	
	@Then("CASEG The system returns the above expected")
	public void caseg_the_system_returns_the_status_PENDING(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEG the amount substracting the fee")
	public void caseg_the_amount_substracting_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		Double esperado = amount - fee;
		assertEquals(esperado, response.getBody().getAmount());
	}

	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	//**********************************************************************************
	@Given("CASEH A transaction that is stored in our system with this payload")
	public void caseh_a_transaction_that_is_stored_in_our_system(DataTable payload) {
		List<Map<String, String>> list = payload.asMaps(String.class, String.class);
		request.setReference(list.get(0).get("reference"));
		request.setChannel(list.get(0).get("channel"));
	}
	
	@When("CASEH I check the status from INTERNAL channel")
	public void caseh_i_check_the_status_from_INTERNAL_channel() {
		response = getStatusResponse(request);
		status = response.getStatusCode();
	}
	
	@And("CASEH the transaction date is after today")
	public void casee_the_transaction_date_is_after_today() {
		LocalDate today = LocalDate.now();
		TransactionDto transaction = getTransactionByReference(response.getBody().getReference());
		LocalDate transactionDate = new LocalDate(transaction.getDate());
		assertTrue(today.compareTo(transactionDate) < 0);
	}
	
	@Then("CASEH The system returns the above expected")
	public void caseh_the_system_returns_the_status_INTERNAL(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		
		assertEquals(HttpStatus.OK, status);
		assertNotNull(response.getBody());
		assertEquals(list.get(0).get("reference"), response.getBody().getReference());
		assertEquals(list.get(0).get("status"), response.getBody().getStatus());

	}
	
	@And("CASEH the amoount")
	public void caseh_the_amount(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double amount = Double.parseDouble(list.get(0).get("amount"));
		assertEquals(amount, response.getBody().getAmount());
	}

	@And("CASEH the fee")
	public void caseh_the_fee(DataTable expected) {
		List<Map<String, String>> list = expected.asMaps(String.class, String.class);
		Double fee = Double.parseDouble(list.get(0).get("fee"));
		assertEquals(fee, response.getBody().getFee());
	}
	
	
}
