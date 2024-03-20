package com.example.notes_jetpackcompose

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme
import com.example.notes_jetpackcompose.ui.theme.Orange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mainViewModel.getNotes()
            Handler(Looper.getMainLooper()).postDelayed({
                mainViewModel.addNote(
                    Note(
                        id= 0,
                        title = "New Note",
                        content = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                    )
                )
            }, 2000)


//        CoroutineScope(Dispatchers.IO).launch{
//
//            delay(2000)
//            mainViewModel.addNote(
//                Note(
//                    id= 0,
//                    title = "New Note",
//                    content = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
//                )
//            )
//        }
        setContent {
            NotesJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box (modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)){

                        NotesList( onNoteClick = {
                            editNotes(it)
                        })
                        IconButton(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AddEditNotesActivity::class.java
                                    ).putExtra("isEdit", false)
                                )
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
                }
            }
        }
    }

    fun editNotes(note: Note){
        startActivity(
            Intent(
                this@MainActivity,
                AddEditNotesActivity::class.java
            ).apply {
                putExtra("isEdit", true)
                putExtra("noteId", note.id)
            }
        )
    }

}

@Composable
fun NotesList(mainViewModel: MainViewModel=viewModel(), onNoteClick: (Note) -> Unit) {
    val notes = mainViewModel.notesStateFlow.collectAsState()
    when(notes.value){
        is CustomResponse.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp),){
                items(items = (notes.value as CustomResponse.Success).notesList){
                    note ->
                    Box(modifier = Modifier.clickable {
                        onNoteClick(note)
                    }) {
                        NotesItem(title = note.title, content = note.content)
                    }
                }
            }
        }

        is CustomResponse.Error -> {

        }
        CustomResponse.Loading -> {

        }
    }

}



@Preview(showBackground = true)
@Composable
fun NotesListPreview() {
    NotesJetpackComposeTheme {
        Box (
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp),){
                items(20){
                    NotesItem(title = "Title", content = "Content")
                }
            }
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier

                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .clip(CircleShape)
                    .background(Orange)
                    .padding(5.dp)

                ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = Color.White,

                )
            }
        }
    }
}

