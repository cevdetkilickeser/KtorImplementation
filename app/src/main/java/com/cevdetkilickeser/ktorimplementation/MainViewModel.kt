package com.cevdetkilickeser.ktorimplementation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiClient: ApiClient
) : ViewModel() {

    var productList = MutableStateFlow<MainUiState<List<Product>>>(value = MainUiState.Loading())
        private set

    init {
        viewModelScope.launch {
            apiClient.getProducts().onSuccess { list ->
                productList.update {
                    MainUiState.Success(data = list)
                }
            }.onError {
                productList.update { error ->
                    MainUiState.Error(message = error.toString())
                }
            }
        }
    }
}