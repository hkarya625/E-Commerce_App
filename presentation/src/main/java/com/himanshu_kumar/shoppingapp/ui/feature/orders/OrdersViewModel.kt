package com.himanshu_kumar.shoppingapp.ui.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.OrdersData
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.OrderListUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
   private val orderListUseCase: OrderListUseCase,
    private val appSession: AppSession
):ViewModel() {

    private val _orderState = MutableStateFlow<OrderEvent>(OrderEvent.Loading)
    val orderState = _orderState

    val userId = appSession.getUser().toLong()

    init {
        getOrderList()
    }

    fun filterOrders(list:List<OrdersData>,status:String):List<OrdersData>{
        val filteredList = list.filter { it.status == status }
        return filteredList
    }


    private fun getOrderList() {
        viewModelScope.launch {
            when(val result = orderListUseCase.execute(userId)){
                is ResultWrapper.Success ->{
                    val data = result.value
                    _orderState.value = OrderEvent.Success(data.data)
                }
                is ResultWrapper.Failure ->{
                    _orderState.value = OrderEvent.Error(result.message)
                }
            }
        }
    }
}

sealed class OrderEvent{
    data object Loading:OrderEvent()
    data class Success(val data:List<OrdersData>):OrderEvent()
    data class Error(val errorMsg:String):OrderEvent()
}