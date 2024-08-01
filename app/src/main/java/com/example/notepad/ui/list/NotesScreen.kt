package com.example.notepad.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNoteList

@Composable
fun NotesScreen() {

    val viewModel = LocalContext.current.getViewModel<NotesViewModel>()
    val uiState: NotesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is NotesUiState.Loading -> {}
        is NotesUiState.Error -> {}
        is NotesUiState.Success -> SuccessScreen(state.notes)
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreen(notes: List<Note> = mockNoteList) {
    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = { NotesTopBar() },
        content = { padding -> NotesContent(padding, notes) },
        floatingActionButton = { },
    )
}

@Composable
fun NotesTopBar() {
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 24.dp),
        text = stringResource(R.string.notes_title),
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun NotesContent(
    padding: PaddingValues = PaddingValues(),
    notes: List<Note> = mockNoteList,
) {

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .padding(vertical = padding.calculateTopPadding(), horizontal = 16.dp)
            .fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            items(notes) { note ->
                ItemNote(note)
            }
        })
}

@Composable
fun ItemNote(
    note: Note,
) {
    val color = Color(android.graphics.Color.parseColor(note.color))
    val height = 100.dp * note.heightFactor

    Card(
        shape = Shapes().medium,
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color)
                .padding(16.dp)
                .height(height)
                .fillMaxSize() // Fill the available space
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                color = Color.Black
            )
        }
    }
}