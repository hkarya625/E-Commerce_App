package com.himanshu_kumar.shoppingapp.di

import com.himanshu_kumar.shoppingapp.ui.feature.cart.CartViewModel
import com.himanshu_kumar.shoppingapp.ui.feature.home.HomeViewModel
import com.himanshu_kumar.shoppingapp.ui.feature.product_details.ProductDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get()
        )                    // Inject dependency into HomeViewModel
    }
    viewModel {
        ProductDetailsViewModel(get())
    }

    viewModel {
        CartViewModel(get(), get(), get())
    }
}