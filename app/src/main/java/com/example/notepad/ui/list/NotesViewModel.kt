package com.example.notepad.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.list.DeleteNotesUseCase
import com.example.domain.usecase.list.GetNotesUseCase
import com.example.domain.usecase.list.UpdateNotesUseCase
import com.example.model.entities.Note
import com.example.model.utils.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    data object Error : NotesUiState()
}

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val getNotesUseCase: GetNotesUseCase,
    private val updateNotesUseCase: UpdateNotesUseCase,
    private val deleteNotesUseCase: DeleteNotesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private fun setSuccessState(notes: List<Note>) {
        _uiState.value = when (val state = _uiState.value) {
            is NotesUiState.Success -> NotesUiState.Success(notes, state.itemsView)
            else -> NotesUiState.Success(notes)
        }
    }

    private fun setErrorState() {
        _uiState.value = NotesUiState.Error
    }

    fun getNotes() {
        viewModelScope.launch(dispatcher) {
            getNotesUseCase()
                .catch { setErrorState() }
                .collect { notes ->
                    val pinNotes = notes.filter { it.isPinned }.sortedBy { it.index }
                    val unPinNotes = notes.filter { !it.isPinned }.sortedBy { it.index }
                    setSuccessState(pinNotes + unPinNotes)
                }
        }
    }

    fun updateNotes() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value as NotesUiState.Success) {
                notes.mapIndexed { index, note -> note.copy(index = index) }
            }.also { notes ->
                tryOrError { updateNotesUseCase(notes) }
            }
        }
    }

    fun deleteNotes() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value as NotesUiState.Success) {
                tryOrError { deleteNotesUseCase(notes.filter { it.isChecked }.map { it.id }) }
                getNotes()
            }
        }
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

    fun swipeNotes(oldIndex: Int, newIndex: Int) {
        _uiState.getAndUpdate {
            with((it as NotesUiState.Success)) {
                copy(notes = notes.toMutableList().apply { add(newIndex, removeAt(oldIndex)) })
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

    private suspend fun tryOrError(action: suspend () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            setErrorState()
            Log.e("ROOM ERROR", e.toString())
        }
    }
}