package com.example.notes_jetpackcompose.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes_jetpackcompose.Utils.CustomResponse
import com.example.notes_jetpackcompose.ViewModels.MainViewModel
import com.example.notes_jetpackcompose.Models.Note
import com.example.notes_jetpackcompose.ui.theme.LightOrange
import com.example.notes_jetpackcompose.ui.theme.Orange


@Composable
fun NotesListScreen(onNoteClick: (Note) -> Unit, onAddNoteClick: () -> Unit){
    val mainViewModel: MainViewModel = hiltViewModel()
    val response = mainViewModel.notesStateFlow.collectAsState().value
    LaunchedEffect(Unit){
        mainViewModel.getNotes()
    }

    Box (modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)){
        when(response){
            is CustomResponse.Success -> {
                NotesList(response.notesList){
                    onNoteClick(it)
                }
            }
            is CustomResponse.Error -> {

            }
            is CustomResponse.Loading -> {

            }
        }
        AddIconButton(){
            onAddNoteClick()
        }
    }
}

@Composable
fun NotesList(notesList: List<Note> = emptyList(), onNoteClick: (Note) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), ){
        items(items = notesList){ note ->
            Box(modifier = Modifier.clickable { onNoteClick(note)}) {
                NotesItem(note)
            }
        }
    }

}

@Composable
fun NotesItem(note: Note) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .shadow(4.dp, clip = true)
            .background(LightOrange)
            .padding(16.dp)
            .padding(start = 8.dp)
        ,

        ) {

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = note.title,
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = note.content,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis)

    }
}
@Composable
fun BoxScope.AddIconButton(onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(20.dp)
            .align(Alignment.BottomEnd)
            .clip(CircleShape)
            .background(Orange)
            .padding(5.dp)
    ) {

        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = Color.White
        )
    }
}