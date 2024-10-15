package com.example.notepad.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.list.DeleteNotesUseCase
import com.example.domain.usecase.list.GetNotesUseCase
import com.example.domain.usecase.list.UpdateNotesUseCase
import com.example.model.entities.Note
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

    fun  asSuccess() = this as Success
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
                .catch {
                    Log.e("ROOM ERROR", it.toString())
                    setErrorState()
                }
                .collect { notes -> setSuccessState(getSortedNotes(notes)) }
        }
    }

    private fun getSortedNotes(notes: List<Note>): List<Note> {
        val (pinNotes, unPinNotes) = notes.partition { it.isPinned }
        return pinNotes.sortedBy { it.index } + unPinNotes.sortedBy { it.index }
    }

    fun updateNotes() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value.asSuccess()) {
                notes.mapIndexed { index, note -> note.copy(index = index) }
            }.also { notes ->
                tryOrError { updateNotesUseCase(notes) }
            }
        }
    }

    fun deleteNotes() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value.asSuccess()) {
                tryOrError {
                    deleteNotesUseCase(getCheckedNotes(notes))
                    getNotes()
                }
            }
        }
    }

    private fun getCheckedNotes(notes: List<Note>) = notes.filter { it.isChecked }

    fun searchNotes(query: String) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(notes = notes.filter { note -> note.contains(query) })
            }
        }
    }

    fun restoreNotes(originalNotes: List<Note>) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(notes = originalNotes)
            }
        }
    }

    fun swipeNotes(oldIndex: Int, newIndex: Int) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(notes = notes.toMutableList().apply { add(newIndex, removeAt(oldIndex)) })
            }
        }
    }

    fun checkNote(id: String) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(notes = notes.map { note ->
                    if (note.id == id) note.copy(isChecked = !note.isChecked) else note
                })
            }
        }
    }

    fun pinUpCheckedNotes() {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                val arePinned = allCheckedArePinned(notes)
                copy(notes = notes.map { note ->
                    if (note.isChecked) note.copy(isPinned = !arePinned, isChecked = false)
                    else note
                }.sortedBy { !it.isPinned })
            }
        }
    }

    private fun allCheckedArePinned(notes: List<Note>) =
        notes.filter { it.isChecked }.all { it.isPinned }

    fun changeItemsView() {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(itemsView = if (itemsView == 2) 1 else 2)
            }
        }
    }

    fun selectAllNotes(select: Boolean) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(notes = notes.map { note -> note.copy(isChecked = select) })
            }
        }
    }

    private suspend fun tryOrError(action: suspend () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            setErrorState()
        }
    }
}