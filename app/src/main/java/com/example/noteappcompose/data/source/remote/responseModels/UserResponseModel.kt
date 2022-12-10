package com.example.noteappcompose.data.source.remote.responseModels

import com.example.noteappcompose.domain.models.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseModel(
    val userID: String,
    val userToken: String,
    val userName: String,
    val email: String
) {
    fun toUserModel(): UserModel = UserModel(token = userToken, email = email)
}