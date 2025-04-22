package com.himanshu_kumar.shoppingapp.ui.feature.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
):ViewModel() {


    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()


    fun register(email:String,password:String, name:String){
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            when(val result = registerUseCase.execute(
                email, password, name
            )){
                is ResultWrapper.Success -> {
                    _registerState.value = RegisterState.Success()
                }
                is ResultWrapper.Failure -> {
                    _registerState.value = RegisterState.Error(result.message)
                }
            }
        }
    }
}

sealed class RegisterState{
    data object Idle:RegisterState()
    data object Loading:RegisterState()
    class Success():RegisterState()
    data class Error(val message:String):RegisterState()
}