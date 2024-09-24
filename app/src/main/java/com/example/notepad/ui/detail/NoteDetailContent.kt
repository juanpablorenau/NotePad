package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.example.model.entities.NoteItemType
import com.example.notepad.R
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockNote
import com.example.notepad.utils.mockNoteItems


@Composable
fun NoteDetailContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
    saveText: (String) -> Unit = { },
    isDarkTheme: Boolean = false,
    addCheckBox: (String?) -> Unit = {},
    updateTextField: (NoteItem) -> Unit = {},
    updateCheckBox: (NoteItem) -> Unit = {},
    deleteTextField: (String) -> Unit = {},
    deleteCheckBox: (String) -> Unit = {},
) {
    val color = getColor(if (isDarkTheme) note.darkColor else note.lightColor)

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
                addCheckBox = addCheckBox,
                updateTextField = updateTextField,
                updateCheckBox = updateCheckBox,
                deleteTextField = deleteTextField,
                deleteCheckBox = deleteCheckBox
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
    var titleFieldValue by remember(note.id) { mutableStateOf(TextFieldValue(note.title)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(0.75f),
            value = titleFieldValue,
            onValueChange = { newText ->
                titleFieldValue = newText
                saveText(titleFieldValue.text)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
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
    addCheckBox: (String?) -> Unit = {},
    updateTextField: (NoteItem) -> Unit = {},
    updateCheckBox: (NoteItem) -> Unit = {},
    deleteTextField: (String) -> Unit = {},
    deleteCheckBox: (String) -> Unit = {},
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

            when (item.type) {
                NoteItemType.TEXT -> TextFieldItem(
                    noteItem = item,
                    currentFocusRequester = currentFocusRequester,
                    previousFocusRequester = previousFocusRequester,
                    updateTextField = updateTextField,
                    deleteTextField = deleteTextField
                )

                NoteItemType.CHECK_BOX -> CheckBoxItem(
                    noteItem = item,
                    currentFocusRequester = currentFocusRequester,
                    previousFocusRequester = previousFocusRequester,
                    addCheckBox = addCheckBox,
                    updateCheckBox = updateCheckBox,
                    deleteCheckBox = deleteCheckBox
                )
            }
        }
    }
}