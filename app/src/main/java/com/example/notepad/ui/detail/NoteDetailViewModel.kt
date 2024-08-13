package com.example.notepad.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetNoteDetailUseCase
import com.example.domain.usecase.InsertNoteUseCase
import com.example.domain.usecase.UpdateNoteUseCase
import com.example.model.entities.Note
import com.example.notepad.theme.*
import com.example.notepad.utils.toHexCode
import dagger.hilt.android.lifecycle.HiltViewModel
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
    data class Error(val error: String) : NoteDetailUiState()
}

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteDetailUseCase: GetNoteDetailUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NoteDetailUiState>(NoteDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun manageNote(id: String) {
        if (id == "new_element") createNewNote() else getNoteById(id)
    }

    private fun createNewNote() {
        viewModelScope.launch(Dispatchers.Main) {
            val note = Note()
            insertNoteUseCase(note)
            getNoteDetailUseCase(note.id)
        }
    }

    private fun getNoteById(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            getNoteDetailUseCase(id)
                .catch { }
                .collect { note -> setSuccessState(note) }
        }
    }

    fun updateNote() {
        viewModelScope.launch(Dispatchers.Main) {
            with(_uiState.value as NoteDetailUiState.Success) {
                updateNoteUseCase(note)
            }
        }
    }

    private fun setSuccessState(note: Note) {
        _uiState.value = NoteDetailUiState.Success(note, getColors())
    }

    private fun getColors() = listOf(
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
}