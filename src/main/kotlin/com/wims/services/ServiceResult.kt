package com.wims.services

sealed class ServiceResult<out T> {
    data class Success<T>(val data: T) : ServiceResult<T>()
    data class NotFound(val message: String) : ServiceResult<Nothing>()
    data class Conflict(val message: String) : ServiceResult<Nothing>()
    data class ValidationError(val message: String) : ServiceResult<Nothing>()
}