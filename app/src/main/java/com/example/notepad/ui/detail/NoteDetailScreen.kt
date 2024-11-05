package com.example.notepad.ui.detail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.FormatType
import com.example.model.enums.NoteColor
import com.example.notepad.components.screens.ErrorScreen
import com.example.notepad.components.screens.LoadingScreen
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNote

@Composable
fun NoteDetailScreen(
    navController: NavHostController,
    noteId: String,
    index: Int,
    isDarkTheme: Boolean = false,
) {
    val viewModel = LocalContext.current.getViewModel<NoteDetailViewModel>()
    val uiState: NoteDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(noteId) {
        viewModel.manageNote(noteId, index)
    }

    LifecycleResumeEffect(noteId) {
        onPauseOrDispose { viewModel.updateNote() }
    }

    when (val state = uiState) {
         NoteDetailUiState.Loading -> LoadingScreen()
         NoteDetailUiState.Error -> ErrorScreen { navController.popBackStack() }
        is NoteDetailUiState.Success -> {
            SuccessScreen(
                note = state.note,
                onBackClick = { navController.popBackStack() },
                saveText = { title -> viewModel.saveText(title) },
                pinUpNote = { viewModel.pinUpNote() },
                deleteNote = {
                    viewModel.deleteNote()
                    navController.popBackStack()
                },
                changeColor = { color -> viewModel.changeColor(color) },
                isDarkTheme = isDarkTheme,
                addTextField = { viewModel.addTextField() },
                addCheckBox = { id -> viewModel.addCheckBox(id) },
                addTable = { viewModel.addTable()},
                updateNoteItem = { textField -> viewModel.updateNoteItem(textField) },
                changeFocusIn = { noteItem -> viewModel.changeFocusIn(noteItem) },
                deleteTextField = { noteItem -> viewModel.deleteTextField(noteItem) },
                deleteNoteItemField = { noteItem -> viewModel.deleteNoteItemField(noteItem) },
                duplicateNote = { viewModel.duplicateNote() },
                applyFormat = { type, formatText -> viewModel.applyFormat(type, formatText) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreen(
    note: Note = mockNote,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (NoteColor) -> Unit = {},
    saveText: (String) -> Unit = { },
    isDarkTheme: Boolean = false,
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
    addTable: () -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
    duplicateNote: () -> Unit = {},
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            NoteDetailTopBar(
                note = note,
                onBackClick = onBackClick,
                changeColor = changeColor,
                pinUpNote = pinUpNote,
                deleteNote = deleteNote,
                duplicateNote = duplicateNote,
                isDarkTheme = isDarkTheme
            )
        },
        content = { padding ->
            NoteDetailContent(
                padding = padding,
                note = note,
                saveText = saveText,
                isDarkTheme = isDarkTheme,
                addTextField = addTextField,
                addCheckBox = addCheckBox,
                updateNoteItem = updateNoteItem,
                changeFocusIn = changeFocusIn,
                deleteTextField = deleteTextField,
                deleteNoteItemField = deleteNoteItemField
            )
        },
        bottomBar = {
            NoteDetailBottomBar(
                isDarkTheme = isDarkTheme,
                note = note,
                addTextField = addTextField,
                addCheckBox = addCheckBox,
                addTable = addTable,
                applyFormat =  applyFormat
            )
        },
    )
}