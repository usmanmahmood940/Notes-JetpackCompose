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
    suspend fun addNotes(note: Note) {
        repository.addNotes(note)
    }
    suspend fun updateNotes(note: Note) {
        repository.updateNotes(note)
    }
    suspend fun deleteNotes(note: Note) {
        repository.deleteNotes(note)
    }
}