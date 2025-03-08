package com.himanshu_kumar.shoppingapp.di

import org.koin.dsl.module

val presentationModule = module {
    includes(viewModelModule)
}