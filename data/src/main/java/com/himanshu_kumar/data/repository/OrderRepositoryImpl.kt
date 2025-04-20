package com.himanshu_kumar.data.repository

import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.repository.OrderRepository

class OrderRepositoryImpl(private val networkService: NetworkService): OrderRepository {
    override suspend fun placeOrder(addressDomainModel: AddressDomainModel): ResultWrapper<Long> {
        return networkService.placeOrder(addressDomainModel, 1)
    }

}