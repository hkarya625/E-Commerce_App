package com.himanshu_kumar.shoppingapp.ui.feature.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.CartSummary
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.CartSummaryUseCase
import com.himanshu_kumar.domain.usecase.PlaceOrderUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import com.himanshu_kumar.shoppingapp.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CartSummaryViewModel(
    private val cartSummaryUseCase: CartSummaryUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase
    ):ViewModel() {
    private val _uiState = MutableStateFlow<CartSummaryEvent>(CartSummaryEvent.Loading)
    val uiState = _uiState


    val userId = AppSession.getUser().toLong()

    init {
        getCartSummary(userId)
    }
    private fun getCartSummary(userId:Long){
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            when(val summary = cartSummaryUseCase.execute(userId)){
                is ResultWrapper.Success->{
                    _uiState.value = CartSummaryEvent.Success(summary.value)
                }
                is ResultWrapper.Failure->{
                    _uiState.value = CartSummaryEvent.Error("Something went wrong")
                }
            }
        }
    }

    public fun placeOrder(userAddress:UserAddress){
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            when(val orderId  = placeOrderUseCase.execute(userAddress.toDomainAddress(), userId)){
                is ResultWrapper.Success->{
                    _uiState.value = CartSummaryEvent.PlaceOrder(orderId.value)
                }
                is ResultWrapper.Failure-> {
                    _uiState.value = CartSummaryEvent.Error("Something went wrong")
                }
            }
        }
    }
}

sealed class CartSummaryEvent{
    data object Loading:CartSummaryEvent()
    data class Error(val message:String):CartSummaryEvent()
    data class Success(val cartSummary: CartSummary):CartSummaryEvent()
    data class PlaceOrder(val orderId:Long):CartSummaryEvent()
}