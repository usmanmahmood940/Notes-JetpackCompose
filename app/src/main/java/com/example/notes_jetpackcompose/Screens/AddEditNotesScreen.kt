package com.example.notes_jetpackcompose.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes_jetpackcompose.ViewModels.AddEditNotesViewModel
import com.example.notes_jetpackcompose.Models.Note
import com.example.notes_jetpackcompose.ui.theme.LightOrange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun AddEditNotesScreen(isEdit: Boolean=false,noteId: Int = 0,onFinish:()->Unit) {
    val addEditNotesViewModel: AddEditNotesViewModel = hiltViewModel()
    var isLoaded by remember { mutableStateOf(true)}
    if(isEdit){
        LaunchedEffect(Unit){
            isLoaded = false
            withContext(Dispatchers.IO) {
                addEditNotesViewModel.getNoteById(noteId)
            }
            isLoaded = true
        }
    }
    if (isLoaded) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            Title(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f), addEditNotesViewModel.noteTitle
            )
            Content(Modifier.weight(8f), addEditNotesViewModel.noteContent)
            BottomButtonsBar(
                isEdit = isEdit,
                onDelete = {
                    CoroutineScope(Dispatchers.IO).launch {
                        addEditNotesViewModel.deleteNote(noteId)
                        withContext(Dispatchers.Main) {
                            onFinish()
                        }
                    }

                },
                onSave = {
                    CoroutineScope(Dispatchers.IO).launch {
                        addEditNotesViewModel.apply {
                            upsertNote(
                                Note(
                                    id = noteId,
                                    title = noteTitle.value,
                                    content = noteContent.value
                                )
                            )
                            withContext(Dispatchers.Main) {
                                onFinish()
                            }
                        }
                    }
                }
            )

        }
    }
    else{
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Loading...", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier, noteTitle: MutableState<String>) {
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
        value = noteTitle.value,
        placeholder = {
            Text(
                "Enter Title", Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        },
        onValueChange = { noteTitle.value = it },
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.Center),

        )
}

@Composable
fun Content(modifier: Modifier = Modifier, noteContent: MutableState<String>) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        value = noteContent.value,
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
fun ColumnScope.BottomButtonsBar(
    isEdit: Boolean = false, onDelete: () -> Unit, onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f)
            .background(LightOrange),
        horizontalArrangement = Arrangement.End
    ) {
        if (isEdit) {
            ActionButton(
                onClick = {
                    onDelete()
                }, contentDescription = "Delete Note", icon = Icons.Default.Delete
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.05f), color = Color.Black
            )
        }
        ActionButton(
            onClick = {
                onSave()
            }, contentDescription = "Save Note", icon = Icons.Default.Done
        )
    }
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
            onClick = { onClick() }, modifier = Modifier.align(Alignment.Center)
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
