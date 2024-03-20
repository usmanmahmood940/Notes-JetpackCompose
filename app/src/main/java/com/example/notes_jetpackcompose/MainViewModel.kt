package com.example.notes_jetpackcompose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository):ViewModel() {
    val notesStateFlow = repository.notesStateFlow

    suspend fun getNotes() {
        repository.getNotes()
    }

    suspend fun updateNotes(note: Note) {
        repository.upsertNote(note)
    }
    suspend fun deleteNotes(noteId: Int) {
        repository.deleteNotes(noteId)
    }
}