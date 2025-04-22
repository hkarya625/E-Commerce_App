package com.himanshu_kumar.shoppingapp.ui.feature.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.CartItemModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.DeleteProductUseCase
import com.himanshu_kumar.domain.usecase.GetCartUseCase
import com.himanshu_kumar.domain.usecase.UpdateQuantityUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartUseCase: GetCartUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase,
    private val deleteItemUseCase: DeleteProductUseCase
) : ViewModel()
{

    private val _uiState = MutableStateFlow<CartEvent>(CartEvent.Loading)
    val uiState = _uiState

    val userId = AppSession.getUser().toLong()

    init {
        getCart()
    }
    fun getCart(){
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            cartUseCase.execute(userId).let { result->
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

    fun incrementQuantity(cartItem: CartItemModel){
        if(cartItem.quantity == 10) return
        updateQuantity(cartItem.copy(quantity = cartItem.quantity+1))
    }
    fun decrementQuantity(cartItem: CartItemModel){
       if(cartItem.quantity == 1) return
       updateQuantity(cartItem.copy(quantity = cartItem.quantity-1))
    }
    private fun updateQuantity(cartItem: CartItemModel){
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            when(val result = updateQuantityUseCase.execute(cartItem, userId)){
                is ResultWrapper.Success->{
                    _uiState.value = CartEvent.Success(result.value.data)
                }
                is ResultWrapper.Failure-> {
                    _uiState.value = CartEvent.Error("Something went wrong")
                }
            }
        }
    }
    fun removeItem(cartItem: CartItemModel){
        viewModelScope.launch {
            _uiState.value = CartEvent.Loading
            when(val result = deleteItemUseCase.execute(cartItem.id, userId )){
                is ResultWrapper.Success-> {
                    _uiState.value = CartEvent.Success(result.value.data)
                }
                is ResultWrapper.Failure->{
                    _uiState.value = CartEvent.Error("Something went wrong")
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