package com.example.noteappcompose.data.source.remote.dataSource

import com.example.noteappcompose.data.source.remote.responseModels.BaseApiResponse
import com.example.noteappcompose.data.source.remote.responseModels.NoteResponseModel
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

    suspend fun getAllNotesKtor(): BaseApiResponse<List<NoteResponseModel>> =
        withContext(Dispatchers.IO) {
            httpClient.get("http://192.168.1.5:4040/notes")
        }

    suspend fun uploadImageKtor(byteArray: ByteArray): BaseApiResponse<String> {
        return withContext(Dispatchers.IO) {
            httpClient.post("http://192.168.1.5:4040/notes/image") {
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

    suspend fun getNoteDetailsKtor(noteId: String): BaseApiResponse<NoteResponseModel> =
        withContext(Dispatchers.IO) {
            httpClient.get {
                url("http://192.168.1.5:4040/notes/$noteId")
                contentType(ContentType.Application.Json)
            }
        }


    suspend fun searchNotesKtor(searchWord: String): BaseApiResponse<List<NoteResponseModel>> =
        withContext(Dispatchers.IO) {
            httpClient.get {
                url("http://192.168.1.5:4040/notes/search")
                contentType(ContentType.Application.Json)
                parameter("search_word", searchWord)
            }
        }
}