package com.himanshu_kumar.shoppingapp.ui.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.himanshu_kumar.domain.model.UserDomainModel
import com.himanshu_kumar.shoppingapp.AppSession
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(
    private val appSession: AppSession
) :ViewModel(){

    private val _uiState = MutableStateFlow<ProfileScreenEvent>(ProfileScreenEvent.Loading)         // MutableStateFlow to hold the UI state, initialized with Loading.
    val uiState: MutableStateFlow<ProfileScreenEvent> = _uiState

    init {
        getUserDetail()
    }

    private fun getUserDetail() {
         val user = appSession.getUserDetails()
        Log.d("ProfileViewModel", "getUserDetail: $user")
        _uiState.value = ProfileScreenEvent.Success(user)
    }
}

sealed class ProfileScreenEvent{
    data object Loading:ProfileScreenEvent()
    class Success(val userDetails:UserDomainModel):ProfileScreenEvent()
    data class Error(val message:String):ProfileScreenEvent()
}