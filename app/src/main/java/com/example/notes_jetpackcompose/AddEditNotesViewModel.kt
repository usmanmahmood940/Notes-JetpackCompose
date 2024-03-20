package com.example.notes_jetpackcompose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(private val repository: NotesRepository): ViewModel(){

    val noteTitle : MutableStateFlow<String> = MutableStateFlow("")
    val noteContent : MutableStateFlow<String> = MutableStateFlow("")
    var noteId:Int = -1


    suspend fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }

    suspend fun deleteNote(noteId: Int) {
        repository.deleteNotes(noteId)
    }

     suspend fun getNoteById(noteId: Int) {
         this.noteId = noteId
         val note = repository.getNoteById(noteId)
         noteTitle.value = note.title
         noteContent.value = note.content
    }

}