package com.example.notes_jetpackcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme
import com.example.notes_jetpackcompose.ui.theme.Orange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
//            mainViewModel.addNotes(
//                Note(
//                    id =0,
//                    title = "My New notes",
//                    content = "These are my new notes"
//                )
//            )
            mainViewModel.getNotes()
        }
        setContent {
            NotesJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {

                        NotesList(mainViewModel = mainViewModel)
                        Box( modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(5.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            AddEditNotesActivity::class.java
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Orange)
                            ) {
                                Icon(
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
    }
}

@Composable
fun NotesList(mainViewModel: MainViewModel) {
    val notes = mainViewModel.notesStateFlow.collectAsState()
    when(notes.value){
        is CustomResponse.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp),){
                items(items = (notes.value as CustomResponse.Success).notesList){
                    note ->
                    NotesItem(title = note.title, content = note.content)
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
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .background(Orange)
                .clip(CircleShape),

            ){
            Icon( imageVector = Icons.Default.Add, contentDescription = "Add Note", tint = Color.White)
        }
    }
}