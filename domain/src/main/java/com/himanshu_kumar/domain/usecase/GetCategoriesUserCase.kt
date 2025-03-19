package com.himanshu_kumar.domain.usecase

import com.himanshu_kumar.domain.repository.CategoryRepository

class GetCategoriesUserCase(private val repository:CategoryRepository) {
    suspend fun execute() = repository.getCategories()
}