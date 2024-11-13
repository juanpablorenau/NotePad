package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.Note
import com.example.model.enums.NoteColor
import com.example.notepad.R
import com.example.notepad.components.Dialog
import com.example.notepad.components.DisplayText
import com.example.notepad.components.MenuItem
import com.example.notepad.utils.getColorFromHex
import com.example.notepad.utils.mockNote


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailTopBar(
    note: Note = mockNote,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (NoteColor) -> Unit = {},
    duplicateNote: () -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showColor by remember { mutableStateOf(false) }
    var deleteButtonClicked by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.clickable { onBackClick() },
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back icon",
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.notes_title),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                )
            }
        }, actions = {
            DisplayText(description = R.string.change_color) {
                IconButton(onClick = { showColor = !showColor }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_color_lens),
                        contentDescription = "More icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DisplayText(description = R.string.options) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "More icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        if (note.isPinned) MenuItem(
                            R.drawable.ic_unpin,
                            stringResource(R.string.unpin)
                        )
                        else MenuItem(R.drawable.ic_pin, stringResource(R.string.pin))
                    },
                    onClick = {
                        showMenu = false
                        pinUpNote()
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_duplicate,
                            stringResource(R.string.duplicate)
                        )
                    },
                    onClick = {
                        showMenu = false
                        duplicateNote()
                    },
                )

                DropdownMenuItem(
                    text = { MenuItem(R.drawable.ic_share, stringResource(R.string.share)) },
                    onClick = { showMenu = false },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_delete_outline,
                            stringResource(R.string.delete),
                            androidx.compose.ui.graphics.Color.Red,
                            androidx.compose.ui.graphics.Color.Red
                        )
                    },
                    onClick = {
                        showMenu = false
                        deleteButtonClicked = true
                    },
                )
            }

            DropdownMenu(
                expanded = showColor,
                onDismissRequest = { showColor = false }
            ) {
                ChangeColorMenu(changeColor, isDarkTheme)
            }
        }
    )

    if (deleteButtonClicked) DeleteNoteDialog(deleteNote) { deleteButtonClicked = false }
}

@Composable
fun DeleteNoteDialog(deleteNote: () -> Unit = {}, action: () -> Unit = {}) {
    Dialog(
        text = stringResource(R.string.delete_question),
        yesAction = {
            deleteNote()
            action()
        },
        noAction = { action() }
    )
}

@Preview(showBackground = true)
@Composable
fun ChangeColorMenu(
    changeColor: (NoteColor) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val colors = remember { NoteColor.entries }

    for (i in 0 until colors.size.div(4)) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            for (j in 0 until 4) {
                ColorItem(item = colors[i * 4 + j], changeColor, isDarkTheme)
                if (j != 3) Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ColorItem(
    item: NoteColor = NoteColor.PALE_YELLOW,
    changeColor: (NoteColor) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val color = remember(item, isDarkTheme) {
        getColorFromHex(if (isDarkTheme) item.darkColor else item.lightColor)
    }

    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(36.dp)
            .clickable { changeColor(item) },
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .fillMaxSize()
        )
    }
}