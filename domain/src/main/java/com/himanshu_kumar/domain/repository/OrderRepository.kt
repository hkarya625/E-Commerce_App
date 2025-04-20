package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.model.OrdersListModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface OrderRepository {
    suspend fun placeOrder(addressDomainModel: AddressDomainModel):ResultWrapper<Long>                    // return orderId
    suspend fun getOrderList():ResultWrapper<OrdersListModel>
}