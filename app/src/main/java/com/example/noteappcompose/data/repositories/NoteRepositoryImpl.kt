package com.example.noteappcompose.data.repositories

import com.example.noteappcompose.data.source.remote.dataSource.NoteSocketDataSource
import com.example.noteappcompose.data.source.remote.dataSource.NotesRemoteDataSource
import com.example.noteappcompose.data.utilities.handleError
import com.example.noteappcompose.domain.models.NoteModel
import com.example.noteappcompose.domain.repositories.NoteRepository
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NoteRepositoryImpl @Inject constructor(
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val noteSocketDataSource: NoteSocketDataSource,
    private val externalScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : NoteRepository {
    override suspend fun getNotes(): List<NoteModel> {
        try {
            val getNotesResult = notesRemoteDataSource.getAllNotes()
            return getNotesResult.data!!.map { it.toNoteModel() }
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }

    override suspend fun getNoteDetails(noteId: String): NoteModel {
        try {
            val getNoteDetailsResult = notesRemoteDataSource.getNoteDetails(noteId)
            return getNoteDetailsResult.data!!.toNoteModel()
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }

    override suspend fun searchNotes(searchWord: String): List<NoteModel> {
        try {
            val getSearchResult = notesRemoteDataSource.searchNotes(searchWord = searchWord)
            return getSearchResult.data!!.map { it.toNoteModel() }
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }

    override suspend fun getNewNotes(): Flow<NoteModel> = withContext(ioDispatcher) {
        noteSocketDataSource.observeNotes()
    }

    override suspend fun insertNote(note: String) {
        return externalScope.launch {
            noteSocketDataSource.sendNote(note)
        }.join()
    }

    override suspend fun uploadImage(imageAsByte: ByteArray, extension: String): String {
        try {
            val uploadImageResult = notesRemoteDataSource.uploadImage(imageAsByte)
            return uploadImageResult.data!!
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }
}