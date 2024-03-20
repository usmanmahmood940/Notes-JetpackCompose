package com.example.notes_jetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NotesRepository):ViewModel() {
    val notesStateFlow = repository.notesStateFlow

     fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotes()
        }
    }

     fun addNote(note: Note) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.addNote(note)
         }
    }
    suspend fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }
    suspend fun deleteNotes(noteId: Int) {
        repository.deleteNotes(noteId)
    }
}