package com.himanshu_kumar.shoppingapp.ui.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.CategoriesListModel
import com.himanshu_kumar.domain.model.ProductListModel
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.GetCategoriesUserCase
import com.himanshu_kumar.domain.usecase.GetProductUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import com.himanshu_kumar.shoppingapp.ui.feature.profile.ProfileScreenEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val categoryUseCase: GetCategoriesUserCase,
    private val appSession: AppSession
    ) : ViewModel() {               // ViewModel for the home screen, manages product data.

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)         // MutableStateFlow to hold the UI state, initialized with Loading.
    val uiState: MutableStateFlow<HomeScreenUIEvents> = _uiState                                    // Exposes the UI state as a read-only StateFlow.

    private val _userDetails = MutableStateFlow<UserDomainModel?>(null)         // MutableStateFlow to hold the UI state, initialized with Loading.
    val userDetails = _userDetails

    init {
        getAllProducts()
        getUserDetail()
    }

    private fun getAllProducts(){
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            val featured = getProducts(null)
            val popularProducts = getProducts(5)
            val categories = getCategories()
            if(featured.isEmpty() && popularProducts.isEmpty() && categories.isEmpty()){
                _uiState.value = HomeScreenUIEvents.Error("Something went wrong")
                return@launch
            }
            _uiState.value = HomeScreenUIEvents.Success(featured, popularProducts, categories)
        }
    }


    private suspend fun getCategories():List<CategoriesListModel>{
        categoryUseCase.execute().let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    return (result).value
                }
                is ResultWrapper.Failure -> {
                    Log.d("Categories", (result).message)
                    return emptyList()
                }
            }
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
    private fun getUserDetail() {
        val user = appSession.getUserDetails()
        Log.d("ProfileViewModel", "getUserDetail: $user")
        _userDetails.value = user
    }
}

sealed class HomeScreenUIEvents {                       // Sealed class representing different UI states for the home screen.

    data object Loading : HomeScreenUIEvents() // Loading state.
    data class Success(
        val featured: List<ProductListModel>,
        val popularProducts: List<ProductListModel>,
        val categories: List<CategoriesListModel>
    ) : HomeScreenUIEvents() // Success state with product list.
    data class Error(val message: String) : HomeScreenUIEvents() // Error state with error message.
}