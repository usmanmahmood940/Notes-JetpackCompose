package com.example.notes_jetpackcompose

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes_jetpackcompose.Utils.Constants.NOTE_ID
import com.example.notes_jetpackcompose.Screens.AddEditNotesScreen
import com.example.notes_jetpackcompose.Screens.NotesListScreen
import com.example.notes_jetpackcompose.Screens.Screens
import com.example.notes_jetpackcompose.Screens.startListening
import com.example.notes_jetpackcompose.ViewModels.AddEditNotesViewModel
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var  recognitionIntent: Intent
    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(this)
    }

    private val viewModel: AddEditNotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }



}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.NotesListScreen.name){
        composable(route=Screens.NotesListScreen.name){
            NotesListScreen(
                onNoteClick = {
                    navController.navigate("${Screens.EditNoteScreen.name}/${it.id}")
                },
                onAddNoteClick = {
                    navController.navigate(Screens.AddNoteScreen.name)
                }
            )
        }
        composable(route=Screens.AddNoteScreen.name){
            AddEditNotesScreen(){
                navController.popBackStack()
            }
        }
        composable(
            route="${Screens.EditNoteScreen.name}/{${NOTE_ID}}",
            arguments = listOf(navArgument(NOTE_ID){
                type = NavType.IntType
            })
        ){
            it.arguments?.getString(NOTE_ID)?.let { noteId ->
                AddEditNotesScreen(isEdit = true,noteId = noteId){
                    navController.popBackStack()
                }
            }
        }
    }
}







@Preview(showBackground = true)
@Composable
fun NotesListPreview() {
    NotesJetpackComposeTheme {

    }
}

