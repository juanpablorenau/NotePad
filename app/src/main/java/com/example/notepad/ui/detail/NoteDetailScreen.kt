package com.example.notepad.ui.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.components.Dialog
import com.example.notepad.components.MenuItem
import com.example.notepad.theme.YellowDark
import com.example.notepad.utils.getColor
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNote

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteDetailScreen(
    navController: NavHostController,
    noteId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    val viewModel = LocalContext.current.getViewModel<NoteDetailViewModel>()
    val uiState: NoteDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.manageNote(noteId)

    when (val state = uiState) {
        is NoteDetailUiState.Loading -> Unit
        is NoteDetailUiState.Error -> {
            ErrorScreen(
                onBackClick = { navController.popBackStack() },
                error = state.error
            )
        }

        is NoteDetailUiState.Success -> {
            SuccessScreen(
                note = state.note,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun ErrorScreen(onBackClick: () -> Unit, error: String) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.generic_error_msg)) },
        text = { Text(error) },
        confirmButton = {
            Text(
                modifier = Modifier.clickable { onBackClick() },
                text = stringResource(R.string.accept),
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = "Error Icon"
            )
        })
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SuccessScreen(
    note: Note = mockNote,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { NoteTopBar(onBackClick = onBackClick) },
        content = { padding ->
            NoteContent(
                padding = padding,
                note = note,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
            )
        },
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
    var deleteButtonClicked by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row {
                Icon(
                    modifier = Modifier.clickable { onBackClick() },
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back icon",
                    tint = YellowDark
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.notes_title),
                    color = YellowDark,
                    fontSize = 24.sp,
                )
            }
        }, actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "More icon",
                    tint = YellowDark
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        MenuItem(R.drawable.ic_pin, stringResource(R.string.pin))
                    },
                    onClick = {
                        showMenu = false
                        pinUpNote()
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(R.drawable.ic_color_lens, stringResource(R.string.change_color))
                    },
                    onClick = {
                        showMenu = false
                        changeColor()
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(R.drawable.ic_delete_outline, stringResource(R.string.delete))
                    },
                    onClick = {
                        showMenu = false
                        deleteButtonClicked = true
                    },
                )
            }
        }
    )

    if (deleteButtonClicked) {
        Dialog(
            text = stringResource(R.string.delete_question),
            yesAction = {
                deleteButtonClicked = false
                deleteNote()
            },
            noAction = { deleteButtonClicked = false })
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        val color = getColor(note.color)

        var titleTextField by remember { mutableStateOf(TextFieldValue("")) }
        var contentTextField by remember { mutableStateOf(TextFieldValue("")) }

        titleTextField = TextFieldValue(note.title)
        contentTextField = TextFieldValue(note.content)

        Card(
            shape = Shapes().medium,
            modifier = Modifier.Companion
                .sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = note.id),
                    animatedVisibilityScope = animatedContentScope
                )
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        value = titleTextField,
                        onValueChange = { newText ->
                          titleTextField = newText
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = color,
                            unfocusedIndicatorColor = color,
                            focusedContainerColor = color,
                            unfocusedContainerColor = color
                        ),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )
                    )

                    if (note.isPinned) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.ic_pin),
                            contentDescription = "Pinned icon",
                            tint = Color.Black
                        )
                    }

                }

                TextField(
                    value = contentTextField,
                    onValueChange = { newText ->
                        contentTextField = newText
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = color,
                        unfocusedIndicatorColor = color,
                        focusedContainerColor = color,
                        unfocusedContainerColor = color
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
