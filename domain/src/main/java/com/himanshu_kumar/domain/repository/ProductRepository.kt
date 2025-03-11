package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.Product
import com.himanshu_kumar.domain.network.ResultWrapper

interface ProductRepository {
    suspend fun getProducts(category:String?): ResultWrapper<List<Product>>
}