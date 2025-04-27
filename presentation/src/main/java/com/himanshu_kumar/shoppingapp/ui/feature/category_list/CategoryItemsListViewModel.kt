package com.himanshu_kumar.shoppingapp.ui.feature.category_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryItemsListViewModel(
    private val useCase: GetProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryItemsListUIEvents>(CategoryItemsListUIEvents.Loading)
    val uiState: StateFlow<CategoryItemsListUIEvents> = _uiState

//    init {
//        getProducts(null) // Load all products initially
//    }

    fun getProductsWithCategory(category: Int) {
        getProducts(category)
    }

    private fun getProducts(category: Int?) {
        viewModelScope.launch {
            _uiState.value = CategoryItemsListUIEvents.Loading
            val result = fetchProducts(category)
            if (result.isEmpty()) {
                _uiState.value = CategoryItemsListUIEvents.Error("Something went wrong")
            } else {
                _uiState.value = CategoryItemsListUIEvents.Success(result)
            }
        }
    }

    private suspend fun fetchProducts(category: Int?): List<ProductListModel> {
        return when (val result = useCase.execute(category)) {
            is ResultWrapper.Success -> result.value
            is ResultWrapper.Failure -> emptyList()
        }
    }
}


sealed class CategoryItemsListUIEvents{
    data object Loading:CategoryItemsListUIEvents()
    data class Success(val data:List<ProductListModel>):CategoryItemsListUIEvents()
    data class Error(val message:String):CategoryItemsListUIEvents()
}