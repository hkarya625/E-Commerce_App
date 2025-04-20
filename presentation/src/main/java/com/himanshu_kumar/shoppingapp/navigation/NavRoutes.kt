package com.himanshu_kumar.shoppingapp.navigation

import com.himanshu_kumar.shoppingapp.model.UiProductModel
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object OrdersScreen

@Serializable
object ProfileScreen

@Serializable
object CartSummaryScreen

@Serializable
data class ProductDetails(val product:UiProductModel)

@Serializable
data class UserAddressRoute(val userAddressWrapper: UserAddressWrapper)