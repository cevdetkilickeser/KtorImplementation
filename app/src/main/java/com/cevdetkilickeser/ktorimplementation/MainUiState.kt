package com.cevdetkilickeser.ktorimplementation

sealed interface MainUiState<T> {
    class Loading<T> : MainUiState<T>
    data class Success<T>(val data: T) : MainUiState<T>
    data class Error<T>(val message: String) : MainUiState<T>
}