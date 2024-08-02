package com.example.notepad.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.theme.LightGray1
import com.example.notepad.theme.YellowDark
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
        floatingActionButton = { AddNoteButton() }
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
    onSearch: (String) -> Unit = {},
    getNotes: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(
            top = padding.calculateTopPadding(),
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        SearchNote(onSearch = onSearch, getNotes = getNotes)
        Spacer(modifier = Modifier.height(16.dp))
        NotesList(notes)
    }
}

@Composable
private fun SearchNote(onSearch: (String) -> Unit, getNotes: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = LightGray1, unfocusedContainerColor = LightGray1
        ),
        placeholder = {
            Text(
                modifier = Modifier.fillMaxHeight(),
                text = stringResource(R.string.search),
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = Color.Gray
            )
        },
        value = searchText,
        onValueChange = { newText -> searchText = newText },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            onSearch(searchText.lowercase())
        }),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (searchText.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = Color.Gray,
                    modifier = Modifier.clickable(onClick = {
                        searchText = ""
                        keyboardController?.hide()
                        getNotes()
                    })
                )
            }
        })

}

@Composable
fun NotesList(notes: List<Note>) {
    val notePositions =
        remember { mutableStateListOf(*notes.map { IntOffset(0, 0) }.toTypedArray()) }

    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
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

    var isDragging by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(notePositions[index].x.toFloat()) }
    var offsetY by remember { mutableFloatStateOf(notePositions[index].y.toFloat()) }

    Card(
        shape = Shapes().medium,
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        notePositions[index] = IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                    }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(if (isDragging) Color.LightGray else color)
                .padding(16.dp)
                .height(height)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (note.isPinned) {
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd),
                        painter = painterResource(id = R.drawable.ic_pin),
                        contentDescription = "Pinned icon",
                        tint = Color.Black
                    )
                }
            }

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

@Composable
fun AddNoteButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        containerColor = Color.White,
        onClick = { onClick }
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add_icon",
            tint = YellowDark
        )
    }
}