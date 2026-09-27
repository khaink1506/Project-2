package com.devon.building.converter;

import com.devon.building.entity.TransactionEntity;
import com.devon.building.enums.Transaction;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.model.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConverter {

    private final ModelMapper modelMapper;
    public TransactionEntity toTransactionEntity(TransactionRequest request) {
        TransactionEntity transactionEntity = modelMapper.map(request, TransactionEntity.class);
        transactionEntity.setCode(request.getTransaction().name());
        return transactionEntity;
    }
    public TransactionResponse toTransactionResponse(TransactionEntity entity) {
        TransactionResponse response = modelMapper.map(entity, TransactionResponse.class);
        response.setCustomerId(entity.getCustomer().getId());
        if (entity.getCode() != null) {
            response.setTransactionType(entity.getCode());
        }
        return response;
    }

    public TransactionRequest toTransactionRequest(TransactionEntity entity) {
        TransactionRequest request = modelMapper.map(entity, TransactionRequest.class);
        request.setCustomerId(entity.getCustomer().getId());
        if (entity.getCode() != null) {
            request.setTransaction(Transaction.valueOf(entity.getCode()));
        }
        return request;
    }

}
