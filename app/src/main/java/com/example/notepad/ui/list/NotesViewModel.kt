package com.example.notepad.ui.list

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.example.model.entities.Note
import com.example.notepad.utils.mockNoteList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

sealed class NotesUiState {
    data object Loading : NotesUiState()
    data class Success(val notes: List<Note>) : NotesUiState()
    data class Error(val error: String) : NotesUiState()
}

@HiltViewModel
class NotesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val list =
            (mockNoteList + mockNoteList + mockNoteList + mockNoteList).sortedBy { !it.isPinned }
        _uiState.value = NotesUiState.Success(list)
    }

    fun saveOffSetInNotes(index: Int, offset: IntOffset) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(
                    notes = notes.mapIndexed { i, note ->
                        if (i == index) {
                            note.copy(
                                offsetX = offset.x.toFloat(),
                                offsetY = offset.y.toFloat()
                            )
                        } else note
                    }
                )
            }
        }
    }

    fun checkNote(index: Int) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(
                    notes = notes.mapIndexed { i, note ->
                        if (i == index) note.copy(isChecked = !note.isChecked) else note
                    }
                )
            }
        }
    }
}