package com.himanshu_kumar.domain.network

import com.himanshu_kumar.domain.model.CategoriesListModel

import com.himanshu_kumar.domain.model.ProductListModel

interface NetworkService{
    suspend fun getProducts(category:Int?):ResultWrapper<List<ProductListModel>>
    suspend fun getCategories():ResultWrapper<List<CategoriesListModel>>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value:T):ResultWrapper<T>()
    data class Failure(val message:String):ResultWrapper<Nothing>()
}
