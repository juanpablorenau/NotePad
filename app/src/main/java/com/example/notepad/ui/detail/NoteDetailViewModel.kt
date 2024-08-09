package com.example.notepad.ui.detail

import androidx.lifecycle.ViewModel
import com.example.model.entities.Note
import com.example.notepad.utils.mockNote
import com.example.notepad.utils.mockNoteList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class NoteDetailUiState {
    data object Loading : NoteDetailUiState()
    data class Success(val note: Note) : NoteDetailUiState()
    data class Error(val error: String) : NoteDetailUiState()
}

@HiltViewModel
class NoteDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun manageNote(id: String) {
        if (id == "new_element") {
            _uiState.value = NoteDetailUiState.Success(Note())
        }
        else getNoteById(id)
    }

    private fun getNoteById(id: String) {
        mockNoteList.firstOrNull { it.id == id}?.let {  note ->
            _uiState.value = NoteDetailUiState.Success(note)
        } ?: run {
            _uiState.value = NoteDetailUiState.Error("Note not found + $id")
        }
    }
}