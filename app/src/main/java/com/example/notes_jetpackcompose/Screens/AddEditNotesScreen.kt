package com.example.notes_jetpackcompose.Screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes_jetpackcompose.ViewModels.AddEditNotesViewModel
import com.example.notes_jetpackcompose.Models.Note
import com.example.notes_jetpackcompose.R
import com.example.notes_jetpackcompose.Utils.HelperClass.generateRandomStringWithTime
import com.example.notes_jetpackcompose.ui.theme.LightOrange
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun AddEditNotesScreen(isEdit: Boolean=false,noteId: String? = null,onFinish:()->Unit) {
    val addEditNotesViewModel: AddEditNotesViewModel = hiltViewModel()
    var isLoaded by remember { mutableStateOf(true)}
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val recognitionIntent by remember { mutableStateOf(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)) }

    LaunchedEffect(Unit) {
        setupSpeechRecognizer(speechRecognizer, recognitionIntent, addEditNotesViewModel)
    }
    if(isEdit){
        LaunchedEffect(Unit){
            isLoaded = false
            withContext(Dispatchers.IO) {
                noteId?.let {
                    addEditNotesViewModel.getNoteById(it)
                }
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
                        noteId?.let {
                            addEditNotesViewModel.deleteNote(it)
                        }
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
                                    id = noteId?:generateRandomStringWithTime(),
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
                ,
                speechRecognizer,
                recognitionIntent
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
    isEdit: Boolean = false,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    speechRecognizer: SpeechRecognizer,
    recognitionIntent: Intent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f)
            .background(LightOrange),
        horizontalArrangement = Arrangement.End
    ) {
        ActionButton(
            onClick = { startListening(speechRecognizer,recognitionIntent) },
            contentDescription = "Voice Type",
            icon = ImageVector.vectorResource(id = R.drawable.ic_mic)
        )
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.05f), color = Color.Black
    )
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

fun setupSpeechRecognizer(speechRecognizer: SpeechRecognizer, recognitionIntent: Intent, viewModel: AddEditNotesViewModel) {
    speechRecognizer.setRecognitionListener(speechRecognitionListener(viewModel))
    val supportedLanguages = arrayOf(TranslateLanguage.ENGLISH, TranslateLanguage.HINDI, TranslateLanguage.GERMAN)
    recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, supportedLanguages.joinToString(","))
    recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
}

fun startListening(speechRecognizer: SpeechRecognizer, recognitionIntent: Intent) {
    try {
        speechRecognizer.startListening(recognitionIntent)
    } catch (e: Exception) {
        Log.d("Error", "No voice recognition")
        e.printStackTrace()
    }
}
fun speechRecognitionListener(viewModel: AddEditNotesViewModel): RecognitionListener {
    return object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d("SpeechRecognition", "Ready for speech")
        }

        override fun onBeginningOfSpeech() {
            Log.d("SpeechRecognition", "Beginning of speech")
        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            Log.d("SpeechRecognition", "End of speech")
        }

        override fun onError(error: Int) {
            Log.d("SpeechRecognition", "Error: $error")
        }

        override fun onResults(results: Bundle?) {
            Log.d("SpeechRecognition", "Results received")
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.get(0)?.let {
                viewModel.noteContent.value = it
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d("SpeechRecognition", "Partial results received")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }
}