package com.himanshu_kumar.shoppingapp.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.model.Product
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getProductUseCase: GetProductUseCase): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState: MutableStateFlow<HomeScreenUIEvents> = _uiState
    init {
        getProducts()
    }
    fun getProducts(){
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            getProductUseCase.execute().let {
                result->
                    when(result){
                        is ResultWrapper.Success->{
                            val data = (result as ResultWrapper.Success).value
                            _uiState.value = HomeScreenUIEvents.Success(data)
                        }
                        is ResultWrapper.Failure-> {
                            val error = (result).message?:"An error occurred"
                            _uiState.value = HomeScreenUIEvents.Error(error)
                        }
                    }
            }
        }
    }
}

sealed class HomeScreenUIEvents{
    object Loading: HomeScreenUIEvents()
    data class Success(val data:List<Product>):HomeScreenUIEvents()
    data class Error(val message:String):HomeScreenUIEvents()
}