package com.example.notepad.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.detail.DeleteNoteItemUseCase
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteColor
import com.example.model.utils.getUUID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NoteDetailUiState {
    data object Loading : NoteDetailUiState()
    data class Success(val note: Note) : NoteDetailUiState()
    data object Error : NoteDetailUiState()
}

fun NoteDetailUiState.asSuccess() = this as NoteDetailUiState.Success

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteDetailUseCase: GetNoteDetailUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private fun getNote() = _uiState.value.asSuccess().note

    private fun setSuccessState(note: Note) {
        _uiState.value = NoteDetailUiState.Success(note)
    }

    private fun setErrorState() {
        _uiState.value = NoteDetailUiState.Error
    }

    fun manageNote(id: String, index: Int) {
        if (id.contains("new_element")) insertNote(Note(getUUID(), index))
        else getNoteById(id)
    }

    private fun insertNote(note: Note) {
        viewModelScope.launch(dispatcher) {
            tryOrError { insertNoteUseCase(note) }
            setSuccessState(note)
        }
    }

    private fun getNoteById(id: String) {
        viewModelScope.launch(dispatcher) {
            getNoteDetailUseCase(id)
                .catch { setErrorState() }
                .collect { note -> setSuccessState(note) }
        }
    }

    fun updateNote() {
        viewModelScope.launch(dispatcher) {
            tryOrError { updateNoteUseCase(getNote()) }
        }
    }

    fun deleteNote() {
        viewModelScope.launch(dispatcher) {
            tryOrError { deleteNoteUseCase(getNote()) }
        }
    }

    fun pinUpNote() {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.copy(isPinned = !note.isPinned))
            }
        }
    }

    fun changeColor(color: NoteColor) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(
                    note = note.copy(
                        lightNoteColor = color.lightColor,
                        darkNoteColor = color.darkColor
                    )
                )
            }
        }
    }

    fun saveText(title: String) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.copy(title = title))
            }
        }
    }

    fun addTextField() {
        viewModelScope.launch(dispatcher) {
            _uiState.getAndUpdate { state ->
                with((state.asSuccess())) {
                    copy(note = note.addTextField())
                }
            }
        }
    }

    fun addCheckBox(noteItemId: String?) {
        viewModelScope.launch(dispatcher) {
            _uiState.getAndUpdate { state ->
                with((state.asSuccess())) {
                    copy(note = note.addCheckbox(noteItemId))
                }
            }
        }
    }

    fun addTable() {
        viewModelScope.launch(dispatcher) {
            _uiState.getAndUpdate { state ->
                with((state.asSuccess())) {
                    copy(note = note.addTable())
                }
            }
        }
    }

    fun updateNoteItem(noteItem: NoteItem) {
        viewModelScope.launch(dispatcher) {
            _uiState.getAndUpdate { state ->
                with((state.asSuccess())) {
                    copy(note = note.updateNoteItem(noteItem))
                }
            }
        }
    }

    fun changeFocusIn(noteItem: NoteItem) {
        viewModelScope.launch(dispatcher) {
            _uiState.getAndUpdate { state ->
                with((state.asSuccess())) {
                    copy(note = note.changeFocusIn(noteItem))
                }
            }
        }
    }

    private fun deleteNoteItem(noteItem: NoteItem) {
        viewModelScope.launch(dispatcher) {
            tryOrError { deleteNoteItemUseCase(noteItem) }
        }
    }

    fun deleteTextField(noteItem: NoteItem) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                deleteNoteItem(noteItem)
                copy(note = note.deleteTextField(noteItem.id))
            }
        }
    }

    fun deleteNoteItemField(noteItem: NoteItem) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                deleteNoteItem(noteItem)
                copy(note = note.deleteCheckbox(noteItem.id))
            }
        }
    }

    fun duplicateNote() {
        updateNote()
        insertNote(getNote().duplicate())
    }

    fun applyFormat(formatText: FormatText) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.applyFormat(formatText))
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