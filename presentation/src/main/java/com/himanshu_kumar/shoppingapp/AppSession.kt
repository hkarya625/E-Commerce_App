package com.himanshu_kumar.shoppingapp

import android.content.Context
import com.himanshu_kumar.domain.model.UserDomainModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppSession(private val context: Context){

    fun storeUser(user: UserDomainModel) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("id", user.id)
            putString("userName", user.name)
            putString("email", user.email)
            putString("avatar", user.avatar)
            apply()
        }
    }

    fun getUser(): Int {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getInt("id", 0)
    }
    fun getUserDetails():UserDomainModel{
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return UserDomainModel(
            id = sharedPref.getInt("id", 0),
            name = sharedPref.getString("userName", "") ?: "",
            password = "",
            avatar = sharedPref.getString("avatar", "") ?: "",
            role = "",
            email = sharedPref.getString("email", "") ?: ""
        )
    }
}
