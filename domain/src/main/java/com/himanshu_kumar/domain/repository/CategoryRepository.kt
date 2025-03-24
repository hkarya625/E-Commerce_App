package com.himanshu_kumar.domain.repository

import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories():ResultWrapper<List<CategoriesListModel>>
}