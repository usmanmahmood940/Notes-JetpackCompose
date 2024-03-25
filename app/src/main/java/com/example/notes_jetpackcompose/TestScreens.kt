package com.example.notes_jetpackcompose

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TextInput() {
    var state by remember { mutableStateOf("") }

    TextField(value = state, onValueChange = { state = it }, label = { Text("Enter text") })
}

@Preview
@Composable
fun Recomposable(){
    var state by remember { mutableStateOf(0.0) }
    Log.d("USMAN-TAG","Logged during Initial Composition")
    Button(onClick = { state = Math.random() }) {
        Log.d("USMAN-TAG","Logged during both Composition and Recomposition")
        Text(state.toString())
    }

}
