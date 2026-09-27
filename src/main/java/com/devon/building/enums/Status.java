package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum Status {

    CHUA_XU_LY("Chưa xử lý"),
    DANG_XU_LY("Đang xử lý"),
    DA_XU_LY("Đã xử lý");

    private final String statusName;

    Status(String name){
        this.statusName = name;
    }
    public static Map<String, String> getStatus(){
        Map<String, String> status = new LinkedHashMap<>();
        for(Status s : Status.values()){
            status.put(s.toString(), s.statusName);
        }
        return status;
    }

}
