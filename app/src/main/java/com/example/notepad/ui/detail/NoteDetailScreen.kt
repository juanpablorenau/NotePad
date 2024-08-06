package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.utils.getColor
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNote

@Composable
fun NoteDetailScreen(navController: NavHostController, noteId: String) {

    val viewModel = LocalContext.current.getViewModel<NoteDetailViewModel>()
    val uiState: NoteDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }

    when (val state = uiState) {
        is NoteDetailUiState.Loading -> Unit
        is NoteDetailUiState.Error -> Unit
        is NoteDetailUiState.Success -> SuccessScreen(note = state.note,
            onBackClick = { navController.popBackStack() })
    }
}

@Composable
fun SuccessScreen(
    note: Note = mockNote,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { NoteTopBar(note = note, onBackClick = onBackClick) },
        content = { padding -> NoteContent(padding = padding, note = note) },
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    note: Note = mockNote,
    onBackClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row {
                Icon(
                    modifier = Modifier.clickable { onBackClick() },
                    painter = painterResource(id = R.drawable.ic_baclk),
                    contentDescription = "Back icon",
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.notes_title),
                    color = Color.Black,
                    fontSize = 24.sp,
                )
            }
        }, actions = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NoteContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
) {
    val color = getColor(note.color)

    var titleTextState by remember { mutableStateOf(TextFieldValue(note.title)) }
    var contentTextState by remember { mutableStateOf(TextFieldValue(note.content)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding())
            .background(color)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = titleTextState,
            onValueChange = { newText ->
                titleTextState = newText
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = color,
                unfocusedIndicatorColor = color,
                focusedContainerColor = color,
                unfocusedContainerColor = color
            ),
            maxLines = 1,

            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        )

        TextField(
            value = contentTextState,
            onValueChange = { newText ->
                contentTextState = newText
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = color,
                unfocusedIndicatorColor = color,
                focusedContainerColor = color,
                unfocusedContainerColor = color
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
        )
    }
}

