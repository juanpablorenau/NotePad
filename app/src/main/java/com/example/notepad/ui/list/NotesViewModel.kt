package com.example.notepad.ui.list

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.model.entities.Note
import com.example.model.utils.normalize
import com.example.notepad.utils.mockNoteList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

sealed class NotesUiState {
    data object Loading : NotesUiState()
    data class Success(val notes: List<Note>,val itemsView: Int = 2) : NotesUiState()
    data class Error(val error: String) : NotesUiState()
}

@HiltViewModel
class NotesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var list = listOf<Note>()

    init {
        list = (mockNoteList + mockNoteList + mockNoteList + mockNoteList).sortedBy { !it.isPinned }
        _uiState.value = NotesUiState.Success(list)
    }

    fun searchNotes(query: String) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                val normalizedQuery = query.normalize()
                copy(notes = notes.filter { note ->
                    note.title.normalize().contains(normalizedQuery, ignoreCase = true) ||
                            note.content.normalize().contains(normalizedQuery, ignoreCase = true)
                })
            }
        }
    }

    fun getNotes() {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = list)
            }
        }
    }

    fun saveOffSetInNotes(index: Int, offset: IntOffset) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.mapIndexed { i, note ->
                    if (i == index) {
                        note.copy(
                            offsetX = offset.x.toFloat(), offsetY = offset.y.toFloat()
                        )
                    } else note
                })
            }
        }
    }

    fun checkNote(index: Int) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.mapIndexed { i, note ->
                    if (i == index) note.copy(isChecked = !note.isChecked) else note
                })
            }
        }
    }

    fun deleteCheckedNotes() {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.filter { note -> !note.isChecked })
            }
        }
    }

    fun pinUpCheckedNotes() {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                val allCheckedArePinned = notes.filter { it.isChecked }.all { it.isPinned }
                copy(notes = notes.map { note ->
                    if (note.isChecked) {
                        note.copy(isPinned = !allCheckedArePinned, isChecked = false)
                    } else note
                }.sortedBy { !it.isPinned })
            }
        }
    }

    fun changeItemsView() {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(itemsView = if (itemsView == 2) 1 else 2)
            }
        }
    }
}