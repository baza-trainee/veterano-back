package com.zdoryk.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = super.getErrorAttributes(request, options);

        //extract the status and put custom error message on the map
        HttpStatus status = HttpStatus.valueOf((Integer) errorResponse.get("status"));

        switch (status) {
            case UNAUTHORIZED:
                errorResponse.put("TOKEN", "Missing authorization token");
                break;
            case NO_CONTENT:
                errorResponse.put("TOKEN", "token is missing");
            default:
                errorResponse.put("error",
                                  "We are experiencing temporary server issues." +
                                   " Please try again later.");
        }

        return errorResponse;
    }
}