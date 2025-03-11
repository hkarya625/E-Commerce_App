package com.himanshu_kumar.shoppingapp.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.Product
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getProductUseCase: GetProductUseCase) : ViewModel() {               // ViewModel for the home screen, manages product data.

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)         // MutableStateFlow to hold the UI state, initialized with Loading.
    val uiState: MutableStateFlow<HomeScreenUIEvents> = _uiState                                    // Exposes the UI state as a read-only StateFlow.

    init {
        getAllProducts()
    }

    private fun getAllProducts(){
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            val featured = getProducts("electronics")
            val popularProducts = getProducts("men's clothing")
            if(featured.isEmpty() || popularProducts.isEmpty()){
                _uiState.value = HomeScreenUIEvents.Error("Something went wrong")
                return@launch
            }
            _uiState.value = HomeScreenUIEvents.Success(featured, popularProducts)
        }
    }


    private suspend fun getProducts(category:String?):List<Product> {                                       // Function to fetch products using the use case.
        getProductUseCase.execute(category).let { result ->
            // Executes the use case and handles the result.
            when (result) {
                is ResultWrapper.Success -> {
                    return (result).value
                }
                is ResultWrapper.Failure -> {
                    return emptyList()
                }
            }
        }
    }
}

sealed class HomeScreenUIEvents {                       // Sealed class representing different UI states for the home screen.

    data object Loading : HomeScreenUIEvents() // Loading state.
    data class Success(val featured: List<Product>, val popularProducts: List<Product>) : HomeScreenUIEvents() // Success state with product list.
    data class Error(val message: String) : HomeScreenUIEvents() // Error state with error message.
}