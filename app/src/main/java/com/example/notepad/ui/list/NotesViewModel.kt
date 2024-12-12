package com.example.notepad.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.note.DeleteNotesUseCase
import com.example.domain.usecase.note.GetNotesUseCase
import com.example.domain.usecase.note.UpdateNotesUseCase
import com.example.domain.usecase.preferences.GetColumnsCountUseCase
import com.example.domain.usecase.preferences.SetColumnsCountUseCase
import com.example.model.entities.Note
import com.example.notepad.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NotesUiState {
    data object Loading : NotesUiState()
    data class Success(val notes: List<Note>, val columnsCount: Int = 2) : NotesUiState()
    data object Error : NotesUiState()

    fun asSuccess() = this as Success
}

@HiltViewModel
class NotesViewModel @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val getNotesUseCase: GetNotesUseCase,
    private val updateNotesUseCase: UpdateNotesUseCase,
    private val deleteNotesUseCase: DeleteNotesUseCase,
    private val getColumnsCountUseCase: GetColumnsCountUseCase,
    private val setColumnsCountUseCase: SetColumnsCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")

    private fun setSuccessNotes(notes: List<Note>, columnsCount: Int) {
        _uiState.value = NotesUiState.Success(notes, columnsCount)
    }

    private fun setErrorState() {
        _uiState.value = NotesUiState.Error
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initData() {
        viewModelScope.launch(dispatcher) {
            _query
                .flatMapLatest { query ->
                    combine(getNotesUseCase(query), getColumnsCountUseCase())
                    { notes, columnsCount -> notes to columnsCount }
                }
                .catch { setErrorState() }
                .collect { (notes, columnsCount) -> setSuccessNotes(notes, columnsCount) }
        }
    }

    private fun updateNotes(notes: List<Note>) {
        viewModelScope.launch(dispatcher) {
            tryOrError { updateNotesUseCase(notes) }
        }
    }

    private fun updateColumnsCount(columnsCount: Int) {
        viewModelScope.launch(dispatcher) {
            tryOrError { setColumnsCountUseCase(columnsCount) }
        }
    }

    fun deleteNotes() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value.asSuccess()) {
                tryOrError { deleteNotesUseCase(notes) }
            }
        }
    }

    fun updateQuery(query: String) {
        _query.update { query }
    }

    fun setColumnsCount() {
        with(_uiState.value.asSuccess()) {
            updateColumnsCount(if (columnsCount == 2) 1 else 2)
        }
    }

    fun pinUpCheckedNotes() {
        _uiState.update { state ->
            with((state.asSuccess())) {
                val arePinned = allCheckedArePinned(notes)
                copy(notes = notes
                    .map { note ->
                        if (note.isChecked) note.copy(isPinned = !arePinned, isChecked = false)
                        else note
                    }
                    .sortedBy { !it.isPinned }
                    .also { notes -> updateNotes(notes) }
                )
            }
        }
    }

    private fun allCheckedArePinned(notes: List<Note>) =
        notes.filter { it.isChecked }.all { it.isPinned }

    fun swipeNotes(oldIndex: Int, newIndex: Int) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(notes = notes.toMutableList()
                    .apply { add(newIndex, removeAt(oldIndex)) }
                    .mapIndexed { index, note -> note.copy(index = index) }
                    .also { notes -> updateNotes(notes) }
                )
            }
        }
    }

    fun checkNote(id: String) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(notes = notes.map { note ->
                    if (note.id == id) note.copy(isChecked = !note.isChecked)
                    else note
                })
            }
        }
    }

    fun selectAllNotes(select: Boolean) {
        _uiState.update { state ->
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