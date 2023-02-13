package com.example.noteappcompose.data.utilities

import com.example.noteappcompose.data.source.remote.requestModels.RegisterRequestModel
import com.example.noteappcompose.data.source.remote.responseModels.BaseApiResponse
import com.example.noteappcompose.domain.models.ValidateResult
import io.ktor.client.features.ClientRequestException
import io.ktor.client.statement.readText
import io.ktor.utils.io.charsets.Charset
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend fun ClientRequestException.handleError(): Exception {
    val content = response.readText(Charset.defaultCharset())
    return Exception(Json.decodeFromString<BaseApiResponse<RegisterRequestModel>>(content).message)
}
fun<T> BaseApiResponse<T>.isDataHasGotSuccessfully() = status && data != null
fun ValidateResult.isFieldDataValid() = error == null
