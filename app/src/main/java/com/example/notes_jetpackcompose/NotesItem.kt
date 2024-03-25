package com.example.notes_jetpackcompose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes_jetpackcompose.ui.theme.LightGray
import com.example.notes_jetpackcompose.ui.theme.LightOrange
import com.example.notes_jetpackcompose.ui.theme.NotesJetpackComposeTheme

@Composable
fun NotesItem(title: String, content: String) {
    Log.d("USMAN-TAG","Item Changed")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .shadow(4.dp, clip = true)
            .background(LightOrange)
            .padding(16.dp)
            .padding(start = 8.dp)
            ,

    ) {

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = title,
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = content,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis)

    }
}

@Preview
@Composable
fun NotesItemPreview() {
        NotesItem("Title", "Content")
}