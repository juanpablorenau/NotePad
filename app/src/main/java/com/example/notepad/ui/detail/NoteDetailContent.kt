package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteItemType
import com.example.notepad.R
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockNote
import com.example.notepad.utils.mockNoteItems


@Preview(showBackground = true)
@Composable
fun NoteDetailContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
    saveText: (String) -> Unit = { },
    isDarkTheme: Boolean = false,
    addCheckBox: (String?) -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    val color = getColor(if (isDarkTheme) note.darkNoteColor else note.lightNoteColor)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + 12.dp,
                start = 12.dp,
                end = 12.dp
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(8.dp)
        ) {
            NoteHeader(note, saveText)

            NoteBody(
                notesItems = note.items,
                isDarkTheme = isDarkTheme,
                addCheckBox = addCheckBox,
                updateNoteItem = updateNoteItem,
                changeFocusIn = changeFocusIn,
                deleteTextField = deleteTextField,
                deleteNoteItemField = deleteNoteItemField
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteHeader(
    note: Note = mockNote,
    saveText: (String) -> Unit = { },
) {
    val focusManager = LocalFocusManager.current
    val title = note.title.ifBlank { stringResource(id = R.string.new_title) }
    var titleFieldValue by remember(note.id) { mutableStateOf(TextFieldValue(title)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BasicTextField(
            modifier = Modifier.padding(24.dp),
            value = titleFieldValue,
            onValueChange = { newText ->
                titleFieldValue = newText
                saveText(titleFieldValue.text)
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.secondary
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
        )

        if (note.isPinned) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = "Pinned icon",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }

    LifecycleResumeEffect(note.id) {
        onPauseOrDispose { saveText(titleFieldValue.text) }
    }
}

@Composable
fun NoteBody(
    notesItems: List<NoteItem> = mockNoteItems,
    isDarkTheme: Boolean = false,
    addCheckBox: (String?) -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val focusRequesters = remember(notesItems) { notesItems.map { FocusRequester() } }
    val focusedItem = notesItems.find { it.isFocused }
    val index = notesItems.indexOf(focusedItem).takeIf { it != -1 } ?: 0

    LaunchedEffect(notesItems.size) {
        if (notesItems.isNotEmpty()) listState.scrollToItem(index)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f),
        state = listState,
    ) {
        itemsIndexed(notesItems, key = { _, item -> item.id }) { index, item ->
            val currentFocusRequester = focusRequesters[index]
            val previousFocusRequester = focusRequesters.getOrNull(index - 1)
            val isPreviousItemTable = notesItems.getOrNull(index - 1)?.isTable() ?: false

            when (item.type) {
                NoteItemType.TEXT -> TextFieldItem(
                    noteItem = item,
                    isDarkTheme = isDarkTheme,
                    currentFocusRequester = currentFocusRequester,
                    previousFocusRequester = previousFocusRequester,
                    updateNoteItem = updateNoteItem,
                    changeFocusIn = changeFocusIn,
                    deleteTextField = deleteTextField
                )

                NoteItemType.CHECK_BOX -> CheckBoxItem(
                    noteItem = item,
                    isDarkTheme = isDarkTheme,
                    currentFocusRequester = currentFocusRequester,
                    previousFocusRequester = previousFocusRequester,
                    addCheckBox = addCheckBox,
                    updateNoteItem = updateNoteItem,
                    changeFocusIn = changeFocusIn,
                    deleteNoteItemField = deleteNoteItemField
                )

                NoteItemType.TABLE -> TableItem(
                    noteItem = item,
                    isDarkTheme = isDarkTheme,
                    isPreviousItemTable = isPreviousItemTable,
                    currentFocusRequester = currentFocusRequester,
                    previousFocusRequester = previousFocusRequester,
                    updateNoteItem = updateNoteItem,
                    changeFocusIn = changeFocusIn,
                    deleteNoteItemField = deleteNoteItemField
                )
            }
        }
    }
}