package com.himanshu_kumar.domain.di

import com.himanshu_kumar.domain.usecase.GetProductUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductUseCase(get()) }           //factory ensures that a new instance of the dependency is created every time it's requested.
}