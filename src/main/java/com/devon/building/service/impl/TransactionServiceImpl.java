package com.devon.building.service.impl;

import com.devon.building.converter.TransactionConverter;
import com.devon.building.entity.CustomerEntity;
import com.devon.building.entity.TransactionEntity;
import com.devon.building.entity.User;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.exception.ResourceNotFoundException;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.model.response.TransactionResponse;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.TransactionRepository;
import com.devon.building.service.TransactionService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final TransactionConverter transactionConverter;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public ResponseDTO createTransaction(TransactionRequest request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new InvalidRequestException("Khách hàng không tồn tại"));
        User user = userService.getUserByUsername(SecurityUtils.getCurrentUsername());
        TransactionEntity transaction = transactionConverter.toTransactionEntity(request);
        transaction.setCustomer(customer);
        transaction.setStaff(user);
        transaction.setActive(true);
        transactionRepository.save(transaction);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Thêm giao dịch thành công");
        responseDTO.setData(transactionConverter.toTransactionResponse(transaction));
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateTransaction(TransactionRequest request) {
        if (request.getId() == null) {
            throw new InvalidRequestException("ID giao dịch không được để trống");
        }
        TransactionEntity transaction = transactionRepository.findById(request.getId())
                .orElseThrow(() -> new InvalidRequestException("Giao dịch không tồn tại"));
        transaction.setNote(request.getNote());
        transactionRepository.save(transaction);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Cập nhật giao dịch thành công");
        responseDTO.setData(transactionConverter.toTransactionResponse(transaction));
        return responseDTO;
    }

    @Override
    public List<TransactionResponse> getTransactions(Long customerId, String code) {
        List<TransactionEntity> transactions = transactionRepository.findByCustomer_IdAndCodeAndIsActiveTrueOrderByCreatedDateDesc(customerId, code);
        return transactions.stream()
                .map(transactionConverter::toTransactionResponse)
                .toList();
    }

    @Override
    public ResponseDTO deleteTransaction(Long id) {
        if (id == null) {
            throw new InvalidRequestException("Không có id giao dịch được cung cấp");
        }
        TransactionEntity transaction= transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id giao dịch"));
        transaction.setActive(false);
        transactionRepository.save(transaction);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Xóa khách hàng thành công");
        return responseDTO;
    }


}
