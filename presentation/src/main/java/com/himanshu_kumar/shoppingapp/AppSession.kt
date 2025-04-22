package com.himanshu_kumar.shoppingapp

import android.content.Context
import com.himanshu_kumar.domain.model.UserDomainModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppSession : KoinComponent {
    private val context: Context by inject()

    fun storeUser(user: UserDomainModel) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("id", user.id)
            putString("userName", user.name)
            putString("email", user.email)
            apply()
        }
    }

    fun getUser(): Int {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getInt("id", 0)
    }
}
