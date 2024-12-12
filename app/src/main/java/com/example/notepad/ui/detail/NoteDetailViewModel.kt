package com.example.notepad.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.formattext.DeleteFormatTextUseCase
import com.example.domain.usecase.note.DeleteNoteUseCase
import com.example.domain.usecase.note.GetNoteUseCase
import com.example.domain.usecase.note.InsertNoteUseCase
import com.example.domain.usecase.note.UpdateNoteUseCase
import com.example.domain.usecase.noteitem.DeleteNoteItemUseCase
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.FormatType
import com.example.model.enums.NoteColor
import com.example.model.enums.ParagraphType
import com.example.model.utils.getUUID
import com.example.notepad.di.MainDispatcher
import com.example.notepad.utils.Constants.NEW_ELEMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NoteDetailUiState {
    data object Loading : NoteDetailUiState()
    data class Success(val note: Note) : NoteDetailUiState()
    data object Error : NoteDetailUiState()

    fun asSuccess() = this as Success
}

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    private val deleteFormatTextUseCase: DeleteFormatTextUseCase
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
        if (id.contains(NEW_ELEMENT)) insertNote(Note(getUUID(), index))
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
            getNoteUseCase(id)
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
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(note = note.copy(isPinned = !note.isPinned))
            }
        }
    }

    fun changeColor(newColor: NoteColor) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(note = note.copy(color = newColor))
            }
        }
    }

    fun saveTitle(title: String) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(note = note.copy(title = title))
            }
        }
    }

    fun addTextField() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { state ->
                with((state.asSuccess())) {
                    copy(note = note.addTextField())
                }
            }
        }
    }

    fun addCheckBox(noteItemId: String?) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { state ->
                with((state.asSuccess())) {
                    copy(note = note.addCheckbox(noteItemId))
                }
            }
        }
    }

    fun addTable() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { state ->
                with((state.asSuccess())) {
                    copy(note = note.addTable())
                }
            }
        }
    }

    fun updateNoteItem(noteItem: NoteItem) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { state ->
                with((state.asSuccess())) {
                    copy(note = note.updateNoteItem(noteItem) { id -> deleteFormatText(id) })
                }
            }
        }
    }

    fun changeFocusIn(noteItem: NoteItem) {
        viewModelScope.launch(dispatcher) {
            _uiState.update { state ->
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
        _uiState.update { state ->
            with((state.asSuccess())) {
                deleteNoteItem(noteItem)
                copy(note = note.deleteTextField(noteItem.id))
            }
        }
    }

    fun deleteNoteItemField(noteItem: NoteItem) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                deleteNoteItem(noteItem)
                copy(note = note.deleteNoteItemField(noteItem.id))
            }
        }
    }

    fun duplicateNote() {
        updateNote()
        insertNote(getNote().duplicate())
    }

    fun applyParagraph(paragraphType: ParagraphType) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(note = note.applyParagraph(paragraphType))
            }
        }
    }

    fun applyFormat(formatType: FormatType, formatText: FormatText) {
        _uiState.update { state ->
            with((state.asSuccess())) {
                copy(note = note.applyFormat(formatType, formatText) { id ->
                    deleteFormatText(id)
                })
            }
        }
    }

    private fun deleteFormatText(formatTextId: String) {
        viewModelScope.launch(dispatcher) {
            tryOrError { deleteFormatTextUseCase(formatTextId) }
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