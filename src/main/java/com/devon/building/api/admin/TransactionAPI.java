package com.devon.building.api.admin;

import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionAPI {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createTransaction(@RequestBody @Valid TransactionRequest request){
        return ResponseEntity.ok().body(transactionService.createTransaction(request));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateTransaction(@RequestBody @Valid TransactionRequest request){
        return ResponseEntity.ok().body(transactionService.updateTransaction(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok().body(transactionService.deleteTransaction(id));
    }
}
