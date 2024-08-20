package com.example.notepad.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.model.entities.Color
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
import com.example.model.entities.Color as AppColor

sealed class NoteDetailUiState {
    data object Loading : NoteDetailUiState()
    data class Success(val note: Note, val colors: List<AppColor>) : NoteDetailUiState()
    data object Error : NoteDetailUiState()
}

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteDetailUseCase: GetNoteDetailUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private fun setSuccessState(note: Note) {
        _uiState.value = NoteDetailUiState.Success(note, AppColor.entries)
    }

    private fun setErrorState() {
        _uiState.value = NoteDetailUiState.Error
    }

    fun manageNote(id: String, index: Int) {
        if (id.contains("new_element")) createNewNote(index) else getNoteById(id)
    }

    private fun createNewNote(index: Int) {
        viewModelScope.launch(dispatcher) {
            val note = Note(
                index = index,
                lightColor = AppColor.PALE_YELLOW.lightColor,
                darkColor = AppColor.PALE_YELLOW.darkColor
            )
            tryOrError {
                insertNoteUseCase(note)
                setSuccessState(note)
            }
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
            with(_uiState.value as NoteDetailUiState.Success) {
                tryOrError { updateNoteUseCase(note) }
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch(dispatcher) {
            with(_uiState.value as NoteDetailUiState.Success) {
                tryOrError { deleteNoteUseCase(note.id) }
            }
        }
    }

    fun pinUpNote() {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(isPinned = !note.isPinned))
            }
        }
    }

    fun changeColor(newColor: AppColor) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(
                    note = note.copy(
                        lightColor = newColor.lightColor,
                        darkColor = newColor.darkColor
                    )
                )
            }
        }
    }

    fun saveText(title: String, content: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(title = title, content = content))
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