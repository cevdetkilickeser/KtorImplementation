package com.cevdetkilickeser.ktorimplementation

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error<T>(val error: Exception) : ApiResponse<T>

    fun onSuccess(block: (T) -> Unit): ApiResponse<T> {
        if (this is Success) block(data)
        return this
    }

    fun onError(block: (Exception) -> Unit): ApiResponse<T> {
        if (this is Error) block(error)
        return this
    }
}