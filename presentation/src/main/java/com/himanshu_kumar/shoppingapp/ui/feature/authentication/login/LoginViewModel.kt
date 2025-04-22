package com.himanshu_kumar.shoppingapp.ui.feature.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_kumar.domain.network.ResultWrapper
import com.himanshu_kumar.domain.usecase.LoginUseCase
import com.himanshu_kumar.shoppingapp.AppSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
):ViewModel() {


    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()


    fun login(email:String,password:String){
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            when(val result = loginUseCase.execute(email, password)){
                is ResultWrapper.Success -> {
                    AppSession.storeUser(result.value)
                    _loginState.value = LoginState.Success()
                }
                is ResultWrapper.Failure -> {
                    _loginState.value = LoginState.Error((result as ResultWrapper.Failure).message?:"Something went wrong")
                }
            }
        }
    }
}

sealed class LoginState{
    data object Idle:LoginState()
    data object Loading:LoginState()
    class Success():LoginState()
    data class Error(val message:String):LoginState()
}