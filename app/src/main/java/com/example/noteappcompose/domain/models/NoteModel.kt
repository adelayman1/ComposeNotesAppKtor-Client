package com.example.noteappcompose.domain.models


data class NoteModel(
    val id: String,
    val noteTitle: String,
    val noteSubtitle: String,
    val noteText: String,
    val dateTime: String,
    val imageLink: String?,
    val noteColor: Int,
    val webLink: String?,
)