package com.himanshu_kumar.domain.di

import com.himanshu_kumar.domain.usecase.GetProductUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductUseCase(get()) }
}