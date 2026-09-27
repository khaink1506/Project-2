package com.devon.building.service;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.model.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    ResponseDTO createTransaction(TransactionRequest request);
    ResponseDTO updateTransaction(TransactionRequest request);
    List<TransactionResponse> getTransactions(Long customerId, String code);
    ResponseDTO deleteTransaction(Long id);

}
