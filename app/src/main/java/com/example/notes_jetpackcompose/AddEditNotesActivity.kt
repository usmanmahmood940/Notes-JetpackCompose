package com.example.notes_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_jetpackcompose.ui.theme.LightOrange
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddEditNotesActivity : ComponentActivity() {

    private val addEditNotesViewModel: AddEditNotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("isEdit") && intent.getBooleanExtra("isEdit", false)) {
            intent.getIntExtra("noteId", -1).let { noteId ->
                CoroutineScope(Dispatchers.IO).launch {
                    addEditNotesViewModel.getNoteById(noteId)
                }
            }
        }
        setContent {
            NotesJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Title(
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .weight(1f),
                                addEditNotesViewModel.noteTitle
                            )
                            Content(Modifier.weight(8f), addEditNotesViewModel.noteContent)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.8f)
                                    .background(LightOrange),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (intent.hasExtra("isEdit") && intent.getBooleanExtra(
                                        "isEdit",
                                        false
                                    )
                                ) {
                                    ActionButton(
                                        onClick = {
                                            addEditNotesViewModel.apply {
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    deleteNote(noteId)
                                                    withContext(Dispatchers.Main) {
                                                        finish()
                                                    }
                                                }
                                            }
                                        },
                                        contentDescription = "Delete Note",
                                        icon = Icons.Default.Delete
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(0.05f),
                                        color = Color.Black
                                    )
                                }
                                ActionButton(
                                    onClick = {
                                        addEditNotesViewModel.apply {
                                            if (noteTitle.value.isNotEmpty() && noteContent.value.isNotEmpty()){
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    upsertNote(
                                                        Note(
                                                            id = intent.getIntExtra("noteId", 0),
                                                            title = noteTitle.value,
                                                            content = noteContent.value
                                                        )
                                                    )
                                                    withContext(Dispatchers.Main) {
                                                        finish()
                                                    }
                                                }
                                             }
                                        }
                                    },
                                    contentDescription = "Save Note",
                                    icon = Icons.Default.Done
                                )
                            }


                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Title(modifier: Modifier = Modifier, noteTitle: MutableStateFlow<String>) {
    val state = noteTitle.collectAsState(initial = "")
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White

        ),
        maxLines = 1,
        value = state.value,
        placeholder = { Text("Enter Title",Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        onValueChange = { noteTitle.value = it },
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center),

        )
}

@Composable
fun Content(modifier: Modifier = Modifier, noteContent: MutableStateFlow<String>) {
    val state = noteContent.collectAsState(initial = "")
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        value = state.value,
        onValueChange = { noteContent.value = it },
        placeholder = { Text("Enter Content") },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),

    )
}

@Composable
fun RowScope.ActionButton(onClick: () -> Unit, contentDescription: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(2f)
            .clickable(onClick = onClick)
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.Black,
                modifier = Modifier.size(50.dp)

            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesJetpackComposeTheme {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .background(LightOrange), horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .clickable {

                        }
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Note",
                            tint = Color.Black,
                            modifier = Modifier.size(50.dp)

                        )

                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.05f),
                    color = Color.Black
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .clickable {

                        }
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save Note",
                            tint = Color.Black,
                            modifier = Modifier.size(50.dp)
                        )

                    }
                }
            }
        }
    }
}