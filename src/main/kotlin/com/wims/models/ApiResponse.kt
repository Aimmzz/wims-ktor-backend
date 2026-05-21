package com.wims.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val errors: List<FieldError>? = null,
)

@Serializable
data class FieldError(
    val field: String,
    val message: String,
)

fun <T> success(data: T, message: String? = null) = ApiResponse(
    success = true,
    message = message,
    data = data
)

fun failed(message: String, errors: List<FieldError>? = null) = ApiResponse<Nothing>(
    success = false,
    message = message,
    errors = errors
)