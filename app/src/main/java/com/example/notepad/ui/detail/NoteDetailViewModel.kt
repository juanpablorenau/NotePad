package com.example.notepad.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.detail.DeleteNoteItemUseCase
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.entities.NoteColor
import com.example.model.entities.NoteItem
import com.example.model.entities.TextColor
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
    data class Success(
        val note: Note,
        val noteColors: List<NoteColor> = NoteColor.entries,
        val textColors: List<TextColor> = TextColor.entries
    ) : NoteDetailUiState()
    data object Error : NoteDetailUiState()

    fun  asSuccess() = this as Success
}

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
            tryOrError {
                insertNoteUseCase(note)
                setSuccessState(note)
            }
        }
    }

    private fun getNoteById(id: String) {
        viewModelScope.launch(dispatcher) {
            getNoteDetailUseCase(id).catch { setErrorState() }
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
            tryOrError { deleteNoteUseCase(getNote().id) }
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
                copy(note = note.copy(lightNoteColor = color.lightColor, darkNoteColor = color.darkColor))
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
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.addTextField())
            }
        }
    }

    fun addCheckBox(noteItemId: String?) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.addCheckbox(noteItemId))
            }
        }
    }

    fun updateTextField(textField: NoteItem) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.updateTextField(textField))
            }
        }
    }

    fun updateCheckBox(checkBox: NoteItem) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                copy(note = note.updateCheckbox(checkBox))
            }
        }
    }

    fun deleteTextField(id: String) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                deleteNoteItem(id)
                copy(note = note.deleteTextField(id))
            }
        }
    }

    private fun deleteNoteItem(id: String) {
        viewModelScope.launch(dispatcher) {
            tryOrError { viewModelScope.launch(dispatcher) { deleteNoteItemUseCase(id) } }
        }
    }

    fun deleteCheckBox(id: String) {
        _uiState.getAndUpdate { state ->
            with((state.asSuccess())) {
                deleteNoteItem(id)
                copy(note = note.deleteCheckbox(id))
            }
        }
    }

    fun copyNote() {
        updateNote()
        insertNote(getNote().copy(getUUID()))
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
            Log.e("ROOM ERROR", e.toString())
        }
    }
}