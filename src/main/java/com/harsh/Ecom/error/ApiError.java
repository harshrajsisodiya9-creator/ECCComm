package com.harsh.Ecom.error;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime localDateTime;
    private String error;
    private HttpStatus statusCode;

    public ApiError(){this.localDateTime = LocalDateTime.now();}

    public ApiError(String error,HttpStatus statusCode){this.error = error;this.statusCode = statusCode;}

    public String getError() {
        return error;
    }

    public HttpStatus getStatusCode(){return statusCode;}
}