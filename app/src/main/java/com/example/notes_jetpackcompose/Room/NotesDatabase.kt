package com.example.demp_productcrud.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demp_Notecrud.Room.NotesDao
import com.example.notes_jetpackcompose.Note

@Database(entities = [Note::class,], version = 2, exportSchema = true)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}