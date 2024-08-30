package com.example.notepad.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.detail.DeleteNoteItemUseCase
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.util.UUID
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
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
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
            val noteId = UUID.randomUUID().toString()
            val noteItemId = UUID.randomUUID().toString()
            val note = Note(
                id = noteId,
                index = index,
                lightColor = AppColor.PALE_YELLOW.lightColor,
                darkColor = AppColor.PALE_YELLOW.darkColor,
                items = listOf(NoteItem(id = noteItemId, noteId = noteId))
            )
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
                        lightColor = newColor.lightColor, darkColor = newColor.darkColor
                    )
                )
            }
        }
    }

    fun saveText(title: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(title = title))
            }
        }
    }

    fun addTextField() {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                if (note.items.isNotEmpty() && note.items.last().isText()) return
                val newNoteItemId = UUID.randomUUID().toString()
                copy(
                    note = note.copy(items = note.items.toMutableList()
                        .apply { add(NoteItem(id = newNoteItemId, noteId = note.id)) })
                )
            }
        }
    }

    fun addCheckBox(noteItemId: String?) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                val newNoteItemId = UUID.randomUUID().toString()
                copy(
                    note = note.copy(items = note.items.toMutableList().apply {
                        val index =
                            if (noteItemId != null) indexOfFirst { item -> item.id == noteItemId } + 1
                            else size
                        add(
                            index, NoteItem(
                                id = newNoteItemId, noteId = note.id, type = NoteItemType.CHECK_BOX
                            )
                        )
                    })
                )
            }
        }
    }

    fun updateTextField(textField: NoteItem) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(items = note.items.map { noteItem ->
                    if (noteItem.id == textField.id) noteItem.copy(text = textField.text)
                    else noteItem
                }))
            }
        }
    }

    fun updateCheckBox(checkBox: NoteItem) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(note = note.copy(items = note.items.map { noteItem ->
                    if (noteItem.id == checkBox.id) {
                        noteItem.copy(text = checkBox.text, isChecked = checkBox.isChecked)
                    } else noteItem
                }))
            }
        }
    }

    fun deleteTextField(id: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(
                    note = note.copy(items = note.items.toMutableList().apply {
                        val index = indexOfFirst { noteItem -> noteItem.id == id }
                        if (index != -1) {
                            removeAt(index)
                            viewModelScope.launch(dispatcher) { deleteNoteItemUseCase(id) }
                        }
                    })
                )
            }
        }
    }

    fun deleteCheckBox(id: String) {
        _uiState.getAndUpdate {
            with((it as NoteDetailUiState.Success)) {
                copy(
                    note = note.copy(items = note.items.toMutableList().apply {
                        val index = indexOfFirst { noteItem -> noteItem.id == id }
                        val prev = getOrNull(index - 1)
                        val next = getOrNull(index + 1)
                        when (index) {
                            -1 -> return@apply
                            size - 1 -> removeLast()
                            else -> {
                                removeAt(index)
                                viewModelScope.launch(dispatcher) { deleteNoteItemUseCase(id) }
                                if (prev?.isText() == true && next?.isText() == true) {
                                    removeAt(index)
                                    removeAt(index - 1)
                                    add(
                                        index - 1,
                                        prev.copy(text = "${prev.text}\n${next.text}")
                                    )
                                }
                            }
                        }
                    })
                )
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