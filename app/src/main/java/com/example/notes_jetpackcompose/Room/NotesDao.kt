package com.example.demp_Notecrud.Room

import androidx.room.*
import androidx.room.Dao
import com.example.notes_jetpackcompose.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertNoteList(notes: List<Note>)

    @Upsert
    suspend  fun upsertNote(note: Note)

    @Query("DELETE FROM Note WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM Note")
    fun getNote(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getNoteById(id: Int): Note
}