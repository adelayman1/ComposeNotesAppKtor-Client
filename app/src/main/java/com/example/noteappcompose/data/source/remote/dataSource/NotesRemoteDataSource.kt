package com.example.noteappcompose.data.source.remote.dataSource

import com.example.noteappcompose.data.source.remote.responseModels.BaseApiResponse
import com.example.noteappcompose.data.source.remote.responseModels.NoteResponseModel
import com.example.noteappcompose.data.utilities.Constants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRemoteDataSource @Inject constructor(var httpClient: HttpClient) {

    suspend fun getAllNotes(): BaseApiResponse<List<NoteResponseModel>> =
        withContext(Dispatchers.IO) {
            httpClient.get("http://$BASE_URL/notes")
        }

    suspend fun uploadImage(byteArray: ByteArray): BaseApiResponse<String> {
        return withContext(Dispatchers.IO) {
            httpClient.post("http://$BASE_URL/notes/image") {
                body = MultiPartFormDataContent(
                    formData {
                        append("file", byteArray, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=image.png")
                        })
                    }
                )
            }
        }
    }

    suspend fun getNoteDetails(noteId: String): BaseApiResponse<NoteResponseModel> =
        withContext(Dispatchers.IO) {
            httpClient.get {
                url("http://$BASE_URL/notes/$noteId")
                contentType(ContentType.Application.Json)
            }
        }

    suspend fun searchNotes(searchWord: String): BaseApiResponse<List<NoteResponseModel>> =
        withContext(Dispatchers.IO) {
            httpClient.get {
                url("http://$BASE_URL/notes/search")
                contentType(ContentType.Application.Json)
                parameter("search_word", searchWord)
            }
        }
}