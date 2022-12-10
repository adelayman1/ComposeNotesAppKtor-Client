package com.example.noteappcompose.data.source.remote.requestModels

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    val email: String,
    val password: String
)