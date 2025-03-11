package com.himanshu_kumar.data.di

import org.koin.dsl.module


// This module acts as the overall container for data-related dependencies.
val dataModule = module {
    includes(networkModule, repositoryModule)
}