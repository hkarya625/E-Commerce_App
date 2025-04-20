package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.OrderRepository

class OrderListUseCase(
    private val repository: OrderRepository
) {
    suspend fun execute() = repository.getOrderList()
}