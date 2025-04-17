package com.himanshu_kumar.data.di

import com.himanshu_kumar.data.repository.CartRepositoryImpl
import com.himanshu_kumar.data.repository.CategoryRepositoryImpl
import com.himanshu_kumar.data.repository.ProductRepositoryImpl
import com.himanshu_kumar.domain.repository.CartRepository
import com.himanshu_kumar.domain.repository.CategoryRepository
import com.himanshu_kumar.domain.repository.ProductRepository
import org.koin.dsl.module


// Repositories handle the logic of retrieving and managing data from various data sources
val repositoryModule = module {                             // where repositories are defined for module of koin
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
}