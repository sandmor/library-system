package com.sandmor.library_system.common.exceptions;

public record ApiErrorResponse(int statusCode, String message, long timestamp) {
}
