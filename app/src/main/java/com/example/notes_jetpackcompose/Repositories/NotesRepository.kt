package com.example.notes_jetpackcompose.Repositories

import android.util.Log
import com.example.demp_productcrud.Room.NotesDatabase
import com.example.notes_jetpackcompose.Utils.CustomResponse
import com.example.notes_jetpackcompose.Models.Note
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDatabase: NotesDatabase,
    private val firestoreRef: FirebaseFirestore,
) {

    private val _notesStateFlow = MutableStateFlow<CustomResponse>(CustomResponse.Loading)
    val notesStateFlow = _notesStateFlow

    private var databaseReference: CollectionReference? = null
    private var valueEventListener: EventListener<QuerySnapshot>? = null
    private var notesListener: ListenerRegistration? = null



    init {
        databaseReference = firestoreRef.collection("Users").document("Usman").collection("Notes")
    }

    suspend fun getNotesRoom() {
        notesDatabase.getNotesDao().getNote().collect { notes ->
            _notesStateFlow.value = CustomResponse.Success(notes)
        }
    }

    suspend fun addNote(note: Note) {
        notesDatabase.getNotesDao().insertNote(note)
    }


    suspend fun upsertNoteInRoom(note: Note) {
        notesDatabase.getNotesDao().upsertNote(note)
    }

    suspend fun deleteNoteRoom(noteId: Int) {
        notesDatabase.getNotesDao().deleteNote(noteId)
    }

    suspend fun getNoteByIdRoom(id: Int): Note {
        return notesDatabase.getNotesDao().getNoteById(id)
    }


    fun getNotes(){
        valueEventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(snapshotlist: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    _notesStateFlow.value = CustomResponse.Error(error.message.toString())
                }
                if (snapshotlist != null) {
                    val notesList: MutableList<Note> = mutableListOf()
                    _notesStateFlow.value = CustomResponse.Success(notesList)
                    for (snapshot in snapshotlist) {
                        val note = snapshot.toObject(Note::class.java)
                        if (note != null) {
                            notesList.add(note)
                        }
                        _notesStateFlow.value = CustomResponse.Success(notesList)
                    }
                }
            }
        }
        notesListener = databaseReference?.addSnapshotListener(valueEventListener!!)
    }
    fun upsertNote(note: Note) {
        databaseReference?.apply {
           document(note.id.toString()).set(note).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("USMAN-TAG", "Note added")
                }
                if (it.exception != null) {
                    Log.d("USMAN-TAG", it.exception!!.message.toString())
                }
            }
        }
    }

    fun removeListener() {
        notesListener?.apply {
            remove()
        }
    }

    suspend fun deleteNotes(noteId: String) {
        try {
            databaseReference?.apply {
                document(noteId).delete().await()
            }
        } catch (e: Exception) {
            Log.e("USMAN-TAG", "Error deleting item: ${e.message}")
        }
    }

    suspend fun getNoteById(noteId : String):Note?{
        return databaseReference?.document(noteId)?.get()?.await()?.toObject(Note::class.java)

    }
}