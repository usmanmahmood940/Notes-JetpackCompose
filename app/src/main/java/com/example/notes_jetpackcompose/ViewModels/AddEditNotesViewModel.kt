package com.example.notes_jetpackcompose.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.notes_jetpackcompose.Models.Note
import com.example.notes_jetpackcompose.Repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(private val repository: NotesRepository): ViewModel(){

    var noteTitle = mutableStateOf("No Title")
    var noteContent = mutableStateOf("")
    var noteId:String? = ""


    fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }

    suspend fun deleteNote(noteId: String) {
        repository.deleteNotes(noteId)
    }

     suspend fun getNoteById(noteId: String) {
         this.noteId = noteId
         repository.getNoteById(noteId)?.apply {
                noteTitle.value = title
                noteContent.value = content
         }
    }

}