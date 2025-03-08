package com.himanshu_kumar.data.di

import com.himanshu_kumar.data.repository.ProductRepositoryImpl
import com.himanshu_kumar.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }
}