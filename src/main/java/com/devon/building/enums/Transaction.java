package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum Transaction {
    CSKH("Chăm sóc khách hàng"),
    DDX("Dẫn đi xem");

    private final String transactionName;
    Transaction(String name){
        this.transactionName = name;
    }
    public static Map<String, String> getTransaction(){
        Map<String, String> transaction = new LinkedHashMap<>();
        for(Transaction s : Transaction.values()){
            transaction.put(s.toString(), s.transactionName);
        }
        return transaction;
    }
}
