package com.himanshu_kumar.shoppingapp.di

import com.himanshu_kumar.shoppingapp.AppSession
import org.koin.dsl.module

val presentationModule = module {
    includes(viewModelModule)
    single { AppSession(get()) }
}