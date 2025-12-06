package com.tansen.common.dto;

import com.tansen.common.constant.ServerResponseCodeConstant;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseUtil {
    public static ApiResponse getFailureResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse getFailureResponse(String message, int code) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getFailureResponse(String message, Object data) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getValidationFailureResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.VALIDATION_ERROR)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getValidationFailureResponse(String message, Object data) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.VALIDATION_ERROR)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getSuccessfulServerResponse(Object data, String message, int code) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> getSuccessfulServerResponseWithData(T data, String message, int code) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        apiResponse.setHttpStatus(HttpStatus.OK);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    public static ApiResponse getTimeoutResponse(String message, Object data) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.TIMEOUT)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.REQUEST_TIMEOUT)
                .timestamp(LocalDateTime.now())
                .build();

    }

    public static ApiResponse getSuccessfulApiResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.SUCCESS)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getSuccessfulApiResponse(Object data, String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.SUCCESS)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> getSuccessfulApiResponseWithData(T data, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ServerResponseCodeConstant.SUCCESS);
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        apiResponse.setHttpStatus(HttpStatus.OK);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    public static ApiResponse getTimeoutApiResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.TIMEOUT)
                .message(message)
                .httpStatus(HttpStatus.REQUEST_TIMEOUT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Object> getNotFoundResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.SUCCESS)
                .message(message)
                .httpStatus(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Object> getBeanValidationFailureResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.VALIDATION_ERROR)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse getUnAuthorized(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message(message)
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse getAuthenticatedApiResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.SUCCESS)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse getUnprocessableEntityResponse(String message) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message(message)
                .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
