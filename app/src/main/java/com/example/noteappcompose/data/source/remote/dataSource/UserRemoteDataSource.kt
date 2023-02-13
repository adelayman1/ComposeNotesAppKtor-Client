package com.example.noteappcompose.data.source.remote.dataSource

import com.example.noteappcompose.data.source.remote.requestModels.LoginRequestModel
import com.example.noteappcompose.data.source.remote.requestModels.RegisterRequestModel
import com.example.noteappcompose.data.source.remote.responseModels.BaseApiResponse
import com.example.noteappcompose.data.source.remote.responseModels.UserResponseModel
import com.example.noteappcompose.data.utilities.Constants.BASE_URL
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
                url("http://$BASE_URL/user/login")
                header("No-Authorization","true")
                contentType(ContentType.Application.Json)
                body = loginRequestModel
            }
        }


    suspend fun register(registerRequestModel: RegisterRequestModel): BaseApiResponse<UserResponseModel> =
        withContext(Dispatchers.IO) {
            httpClient.post {
                url("http://$BASE_URL/user/register")
                header("No-Authorization","true")
                contentType(ContentType.Application.Json)
                body = registerRequestModel
            }
        }
}