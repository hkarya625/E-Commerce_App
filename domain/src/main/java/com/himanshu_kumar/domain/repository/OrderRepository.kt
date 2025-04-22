package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.model.OrdersListModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface OrderRepository {
    suspend fun placeOrder(addressDomainModel: AddressDomainModel, userId:Long):ResultWrapper<Long>                    // return orderId
    suspend fun getOrderList(userId:Long):ResultWrapper<OrdersListModel>
}