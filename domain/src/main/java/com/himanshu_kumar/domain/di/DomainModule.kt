package com.himanshu_kumar.domain.di

import org.koin.dsl.module


val domainModule = module{
    includes(useCaseModule)
}