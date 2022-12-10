package com.example.noteappcompose.data.source.remote.requestModels

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestModel(
    val name: String,
    val email: String,
    val password: String
)