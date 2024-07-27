package com.techwave.olol.dto.reponse;

import lombok.Data;

@Data
public class ApiResponse {

    private Object data;

    public ApiResponse(Object data) {
        this.data = data;
    }
}
