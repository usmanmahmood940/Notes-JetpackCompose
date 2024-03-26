package com.example.notes_jetpackcompose.Utils

import com.example.notes_jetpackcompose.Models.Note

sealed class CustomResponse {
    data class Success(val notesList: List<Note>) : CustomResponse()
    data class Error(val message: String) : CustomResponse()
    object Loading : CustomResponse()
}