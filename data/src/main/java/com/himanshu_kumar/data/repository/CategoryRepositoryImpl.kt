package com.himanshu_kumar.data.repository



import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.network.NetworkService
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.repository.CategoryRepository

class CategoryRepositoryImpl(private val networkService: NetworkService):CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<List<CategoriesListModel>> {
        return networkService.getCategories()
    }
}