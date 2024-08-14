package com.example.notepad.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.model.entities.Note
import com.example.notepad.theme.*
import com.example.notepad.utils.toHexCode
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
    data class Success(val note: Note, val colors: List<String>) : NoteDetailUiState()
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
        _uiState.value = NoteDetailUiState.Success(note, colors)
    }

    private fun setErrorState() {
        _uiState.value = NoteDetailUiState.Error
    }

    fun manageNote(id: String) {
        if (id == "new_element") createNewNote() else getNoteById(id)
    }

    private fun createNewNote() {
        viewModelScope.launch(dispatcher) {
            val note = Note()
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

    fun changeColor(newColor: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(color = newColor))
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

    private val colors by lazy {
        listOf(
            LightPink,
            LightRose,
            LightBlush,
            LightOrange,
            LightYellow,
            PaleYellow,
            LightGreen,
            PaleGreen,
            MintGreen,
            LightSkyBlue,
            LightBlue,
            Lavender,
            LightBrown,
            LightGrayBlue,
            LightGrayGreen,
            White
        ).map { color -> color.toHexCode() }
    }
}