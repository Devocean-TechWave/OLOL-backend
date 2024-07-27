package com.techwave.olol.login.dto.reponse;

import lombok.Data;

@Data
public class ApiResponse {

    private Object data;

    public ApiResponse(Object data) {
        this.data = data;
    }
}
