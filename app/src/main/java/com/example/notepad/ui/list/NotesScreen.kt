package com.example.notepad.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notepad.utils.getViewModel

@Composable
fun NotesScreen() {

    val viewModel = LocalContext.current.getViewModel<NotesViewModel>()
    val uiState: NotesUiState by viewModel.uiState.collectAsStateWithLifecycle()

}