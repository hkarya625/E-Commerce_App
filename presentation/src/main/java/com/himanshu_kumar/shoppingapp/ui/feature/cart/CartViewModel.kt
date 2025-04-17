package com.himanshu_kumar.shoppingapp.ui.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.GetCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val cartUseCase: GetCartUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<CartEvent>(CartEvent.Loading)
    val uiState = _uiState

    init {
        getCart()
    }
    fun getCart(){
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            cartUseCase.execute().let { result->
                when(result){
                    is ResultWrapper.Success->{
                        _uiState.value = CartEvent.Success(result.value.data)
                    }
                    is ResultWrapper.Failure->{
                        _uiState.value = CartEvent.Error("Something went wrong")
                    }
                }
            }
        }
    }
}

sealed class CartEvent{
    data object Loading:CartEvent()
    data class Success(val data:List<CartItemModel>):CartEvent()
    data class Error(val message:String):CartEvent()
}