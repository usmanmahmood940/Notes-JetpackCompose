package com.example.notes_jetpackcompose

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes_jetpackcompose.Utils.Constants.NOTE_ID
import com.example.notes_jetpackcompose.Screens.AddEditNotesScreen
import com.example.notes_jetpackcompose.Screens.NotesListScreen
import com.example.notes_jetpackcompose.Screens.Screens
import com.example.notes_jetpackcompose.Utils.Type
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var scannerLauncher: ActivityResultLauncher<IntentSenderRequest>

    private var showDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                handleActivityResult(result)
            }
        setContent {
            NotesJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(){ enableGalleryImport, pageLimit, selectedMode ->
                        onScanButtonClicked(enableGalleryImport, pageLimit, selectedMode)
                    }
                }
            }
        }
    }

    private fun onScanButtonClicked(enableGalleryImport: Boolean, pageLimit: Int, selectedMode:String) {

        val options =
            GmsDocumentScannerOptions.Builder()
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_BASE)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                .setGalleryImportAllowed(enableGalleryImport)

        when (selectedMode) {
            "Full" -> options.setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            "Base" -> options.setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_BASE)
            "Base with Filler" ->
                options.setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_BASE_WITH_FILTER)
            else -> Log.e(ContentValues.TAG, "Unknown selectedMode: $selectedMode")
        }

        val pageLimitInputText = pageLimit
        if (pageLimitInputText>0) {
            try {
                val pageLimit = pageLimitInputText.toInt()
                options.setPageLimit(pageLimit)
            } catch (e: Throwable) {

                return
            }
        }

        GmsDocumentScanning.getClient(options.build())
            .getStartScanIntent(this)
            .addOnSuccessListener { intentSender: IntentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener() { e: Exception ->

            }
    }

    private fun handleActivityResult(activityResult: ActivityResult) {
        val resultCode = activityResult.resultCode
        val result = GmsDocumentScanningResult.fromActivityResultIntent(activityResult.data)
        if (resultCode == Activity.RESULT_OK && result != null) {

            val pages = result.pages


            result.pdf?.uri?.path?.let { path ->

                val file = File(path)
                val inputStream = file.inputStream()
                val outputStream = openFileOutput("saved_pdf.pdf", MODE_PRIVATE)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

//                val externalUri = FileProvider.getUriForFile(this, packageName + ".provider", File(path))
//
//                val shareIntent =
//                    Intent(Intent.ACTION_SEND).apply {
//                        putExtra(Intent.EXTRA_STREAM, externalUri)
//                        type = "application/pdf"
//                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    }
//                startActivity(Intent.createChooser(shareIntent, "Shared_Info"))
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
//            resultInfo.text = getString(R.string.error_scanner_cancelled)
        } else {
//            resultInfo.text = getString(R.string.error_default_message)
        }
    }

}

@Composable
fun App(onScanButtonClicked:(Boolean, Int, String)->Unit = {_,_,_->}) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.NotesListScreen.name){
        composable(route=Screens.NotesListScreen.name){
            NotesListScreen(
                onNoteClick = {
                    navController.navigate("${Screens.EditNoteScreen.name}/${it.id}")
                },
                onAddNoteClick = {
                    when(it){
                        Type.NOTE.name -> navController.navigate(Screens.AddNoteScreen.name)
                        Type.SCAN.name -> {
                            onScanButtonClicked(true, 0, "Base with Filler")
                        }
                    }
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
            it.arguments?.getInt(NOTE_ID)?.let { noteId ->
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

