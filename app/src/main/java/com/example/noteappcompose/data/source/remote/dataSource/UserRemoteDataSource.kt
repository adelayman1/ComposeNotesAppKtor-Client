package com.example.noteappcompose.data.source.remote.dataSource

import com.example.noteappcompose.data.source.remote.requestModels.LoginRequestModel
import com.example.noteappcompose.data.source.remote.requestModels.RegisterRequestModel
import com.example.noteappcompose.data.source.remote.responseModels.BaseApiResponse
import com.example.noteappcompose.data.source.remote.responseModels.UserResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(var httpClient: HttpClient) {
    suspend fun login(loginRequestModel: LoginRequestModel): BaseApiResponse<UserResponseModel> =
        withContext(Dispatchers.IO) {
            httpClient.post {
                url("http://192.168.1.5:4040/user/login")
                header("No-Authorization","true")
                contentType(ContentType.Application.Json)
                body = loginRequestModel
            }
        }


    suspend fun register(registerRequestModel: RegisterRequestModel): BaseApiResponse<UserResponseModel> =
        withContext(Dispatchers.IO) {
            httpClient.post {
                url("http://192.168.1.5:4040/user/register")
                header("No-Authorization","true")
                contentType(ContentType.Application.Json)
                body = registerRequestModel
            }
        }
}