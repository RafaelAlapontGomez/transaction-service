package com.example.transaction.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction.dtos.StatusRequestDto;
import com.example.transaction.dtos.TransactionDto;
import com.example.transaction.enums.OrderType;
import com.example.transaction.errors.ApiError;
import com.example.transaction.services.TransactionService;
import com.example.transaction.services.exceptions.NeedChannelForStatus;
import com.example.transaction.services.exceptions.NoAccountPresentForThisTransaction;
import com.example.transaction.services.exceptions.NoBalaceForThisTransaction;
import com.example.transaction.services.exceptions.NoRuleForThisCase;

@RestController
@RequestMapping(path = "/transaction")
//@Slf4j
public class TransactionControllerImpl {

	@Autowired
	TransactionService transactionService;

	@PostMapping(path = "/create")
	public ResponseEntity<Object> postController(@Valid @RequestBody TransactionDto transactionDto) {
		TransactionDto response = null;
		try {
			response = transactionService.createTransaction(transactionDto);
		} catch (NoAccountPresentForThisTransaction ex) {
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		} catch (NoBalaceForThisTransaction ex) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		URI location = URI.create(String.format("/transaction/%s", response.getReference()));
		return ResponseEntity.created(location).body(response);
	}

	@GetMapping(path = "/search")
	public ResponseEntity<List<TransactionDto>> getController(
			@RequestParam(name = "iban", required = false) String iban, @RequestParam(name = "order") String order) {
		return ResponseEntity.ok(transactionService.getTransactions(iban, OrderType.getOrderType(order)));
	}

	@GetMapping(path = "/status")
	public ResponseEntity<Object> getStatus(@NotBlank @RequestParam String reference,
			@RequestParam(required = false) String channel) {

		StatusRequestDto request = StatusRequestDto.builder().reference(reference).channel(channel).build();
		try {

			// channel is optional, but if channel is not present the query doesn't work
			if (StringUtils.isEmpty(channel)) {
				throw new NeedChannelForStatus("Necesito un channel para hacer la consulta");
			}

			return ResponseEntity.ok(transactionService.getStatus(request));
		} catch (NoRuleForThisCase ex) {
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		} catch (NeedChannelForStatus ex) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}
	}

	@GetMapping(path = "/{reference}")
	public ResponseEntity<TransactionDto> getTransactionByReference(@PathVariable(value = "reference") String reference) {
		return ResponseEntity.ok(transactionService.getTransactionByReference(reference));
	}

}
