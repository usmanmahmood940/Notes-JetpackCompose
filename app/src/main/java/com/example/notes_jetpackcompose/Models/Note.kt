package com.example.notes_jetpackcompose.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Note")
data class Note(
    @PrimaryKey
    val id: String="",
    val title: String="",
    val content: String=""
)
