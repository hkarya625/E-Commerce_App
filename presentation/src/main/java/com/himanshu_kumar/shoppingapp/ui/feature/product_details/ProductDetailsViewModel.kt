package com.himanshu_kumar.shoppingapp.ui.feature.product_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.request.AddCartRequestModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.AddToCartUseCase
import com.himanshu_kumar.domain.usecase.GetProductUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import com.himanshu_kumar.shoppingapp.model.UiProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val useCase: AddToCartUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val appSession: AppSession)
    :ViewModel() {
    private val _state = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Nothing)
    val state = _state


    private val _similarProducts = MutableStateFlow<List<ProductListModel>>(emptyList())
    val similarProducts = _similarProducts


    val userId = appSession.getUser()
    fun addProductToCart(product: UiProductModel) {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            val result = useCase.execute(
                AddCartRequestModel(
                    productId = product.id,
                    productName = product.title,
                    price = product.price,
                    quantity = 1,
                    userId = userId,
                ),
                userId = userId.toLong()
            )
            Log.d("fakeCart",result.toString())
            when(result){
                is ResultWrapper.Success -> {
                    _state.value = ProductDetailsState.Success("Product added to cart successfully")
                }
                is ResultWrapper.Failure -> {
                    _state.value = ProductDetailsState.Error("Something went wrong")
                }
            }
        }
    }

    fun getSimilarProducts(categoryId:Int){
        viewModelScope.launch {
            _similarProducts.value = getProducts(categoryId)
        }
    }
    private suspend fun getProducts(category:Int?):List<ProductListModel> {                                       // Function to fetch products using the use case.
        getProductUseCase.execute(category).let { result ->
            // Executes the use case and handles the result.
            when (result) {
                is ResultWrapper.Success -> {
                    return (result).value
                }
                is ResultWrapper.Failure -> {
                    return emptyList()
                }
            }
        }
    }
}
sealed class ProductDetailsState {
    data object Loading : ProductDetailsState()
    data object Nothing:ProductDetailsState()
    data class Success(val message: String) : ProductDetailsState()
    data class Error(val message: String) : ProductDetailsState()
}