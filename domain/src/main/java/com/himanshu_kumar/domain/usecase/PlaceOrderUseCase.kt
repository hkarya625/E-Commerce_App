package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.model.AddressDomainModel
import com.himanshu_kumar.domain.repository.OrderRepository

class PlaceOrderUseCase(private val orderRepository: OrderRepository)  {
    suspend fun execute(addressDomainModel: AddressDomainModel) = orderRepository.placeOrder(addressDomainModel)
}