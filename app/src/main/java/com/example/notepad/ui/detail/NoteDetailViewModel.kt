package com.example.notepad.ui.detail

import androidx.lifecycle.ViewModel
import com.example.model.entities.Note
import com.example.notepad.theme.*
import com.example.notepad.utils.mockNoteList
import com.example.notepad.utils.toHexCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

sealed class NoteDetailUiState {
    data object Loading : NoteDetailUiState()
    data class Success(val note: Note, val colors: List<String>) : NoteDetailUiState()
    data class Error(val error: String) : NoteDetailUiState()
}

@HiltViewModel
class NoteDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun manageNote(id: String) {
        if (id == "new_element") {
            _uiState.value = NoteDetailUiState.Success(Note(), getColors())
        } else getNoteById(id)
    }

    private fun getNoteById(id: String) {
        mockNoteList.firstOrNull { it.id == id }?.let { note ->
            _uiState.value = NoteDetailUiState.Success(note, getColors())
        } ?: run {
            _uiState.value = NoteDetailUiState.Error("Note not found + $id")
        }
    }

    private fun getColors() = listOf(
        LightPink,
        LightRose,
        LightBlush,
        LightOrange,
        LightYellow,
        PaleYellow,
        LightGreen,
        PaleGreen,
        MintGreen,
        LightSkyBlue,
        LightBlue,
        Lavender,
        LightBrown,
        LightGrayBlue,
        LightGrayGreen,
        White
    ).map { color -> color.toHexCode() }

    fun pinUpNote() {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(isPinned = !note.isPinned))
            }
        }
    }

    fun changeColor(newColor: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(color = newColor))
            }
        }
    }
}