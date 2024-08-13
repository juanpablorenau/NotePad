package com.example.notepad.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetNotesUseCase
import com.example.model.entities.Note
import com.example.model.utils.add
import com.example.model.utils.normalize
import com.example.model.utils.removeAt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotesUiState {
    data object Loading : NotesUiState()
    data class Success(val notes: List<Note>, val itemsView: Int = 2) : NotesUiState()
    data class Error(val error: String) : NotesUiState()
}

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getNotes() {
        viewModelScope.launch(Dispatchers.Main) {
            getNotesUseCase()
                .catch { }
                .collect { notes -> setSuccessState(notes) }
        }
    }

    private fun setSuccessState(notes: List<Note>) {
        _uiState.value = NotesUiState.Success(notes)
    }

    fun searchNotes(query: String) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                val normalizedQuery = query.normalize()
                copy(notes = notes.filter { note ->
                    note.title.normalize()
                        .contains(normalizedQuery, ignoreCase = true) || note.content.normalize()
                        .contains(normalizedQuery, ignoreCase = true)
                })
            }
        }
    }

    fun swipeNotes(oldIndex: Int, newIndex: Int) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.apply { add(newIndex, removeAt(oldIndex)) })
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

    fun selectAllNotes(select: Boolean) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.map { note -> note.copy(isChecked = select) })
            }
        }
    }
}