package com.example.demp_Notecrud.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.notes_jetpackcompose.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NotesDao {
    @Insert
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertNoteList(notes: List<Note>)

    @Update
    suspend  fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getNote(): Flow<List<Note>>
}