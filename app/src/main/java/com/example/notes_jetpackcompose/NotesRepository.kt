package com.example.notes_jetpackcompose

import com.example.demp_productcrud.Room.NotesDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesDatabase: NotesDatabase) {

    private val _notesStateFlow = MutableStateFlow<CustomResponse>(CustomResponse.Loading)
    val notesStateFlow = _notesStateFlow

    suspend fun getNotes() {
        notesDatabase.getNotesDao().getNote().collect { notes ->
            _notesStateFlow.value = CustomResponse.Success(notes)
        }
    }

    suspend fun addNote(note: Note) {
        notesDatabase.getNotesDao().insertNote(note)
    }


    suspend fun upsertNote(note: Note) {
        notesDatabase.getNotesDao().upsertNote(note)
    }

    suspend fun deleteNotes(noteId: Int) {
        notesDatabase.getNotesDao().deleteNote(noteId)
    }

    suspend fun getNoteById(id: Int): Note {
        return notesDatabase.getNotesDao().getNoteById(id)
    }
}