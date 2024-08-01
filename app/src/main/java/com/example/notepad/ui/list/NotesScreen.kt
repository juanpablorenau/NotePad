package com.example.notepad.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNoteList
import kotlin.math.roundToInt

@Composable
fun NotesScreen() {

    val viewModel = LocalContext.current.getViewModel<NotesViewModel>()
    val uiState: NotesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is NotesUiState.Loading -> Unit
        is NotesUiState.Error -> Unit
        is NotesUiState.Success -> SuccessScreen(state.notes)
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreen(notes: List<Note> = mockNoteList) {
    Scaffold(
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
    val notePositions =
        remember { mutableStateListOf(*notes.map { IntOffset(0, 0) }.toTypedArray()) }

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            ),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            itemsIndexed(notes) { index, note ->
                ItemNote(note, notePositions, index)
            }
        })
}

@Composable
fun ItemNote(note: Note, notePositions: MutableList<IntOffset>, index: Int) {
    val color = Color(android.graphics.Color.parseColor(note.color))
    val height = 100.dp * note.heightFactor

    var offsetX by remember { mutableFloatStateOf(notePositions[index].x.toFloat()) }
    var offsetY by remember { mutableFloatStateOf(notePositions[index].y.toFloat()) }

    Card(
        shape = Shapes().medium,
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    notePositions[index] = IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color)
                .padding(16.dp)
                .height(height)
                .fillMaxSize()
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