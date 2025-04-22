package com.himanshu_kumar.domain.model

data class UserDomainModel(
    val id:Int,
    val email:String,
    val password:String,
    val name:String,
    val role:String,
    val avatar:String
)