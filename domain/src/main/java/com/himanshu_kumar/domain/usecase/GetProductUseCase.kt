package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.ProductRepository

class GetProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(category:String?) = repository.getProducts(category)
}