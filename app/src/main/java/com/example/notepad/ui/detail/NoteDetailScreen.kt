package com.example.notepad.ui.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.components.Dialog
import com.example.notepad.components.MenuItem
import com.example.notepad.components.screens.ErrorScreen
import com.example.notepad.components.screens.LoadingScreen
import com.example.notepad.theme.YellowDark
import com.example.notepad.utils.getColor
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockColorList
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

    LaunchedEffect(noteId) {
        viewModel.manageNote(noteId)
    }

    LifecycleResumeEffect(noteId) {
        onPauseOrDispose { viewModel.updateNote() }
    }

    when (val state = uiState) {
        is NoteDetailUiState.Loading -> LoadingScreen()
        is NoteDetailUiState.Error -> ErrorScreen { navController.popBackStack() }
        is NoteDetailUiState.Success -> {
            SuccessScreen(
                note = state.note,
                colors = state.colors,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onBackClick = { navController.popBackStack() },
                saveText = { title, content -> viewModel.saveText(title, content) },
                pinUpNote = { viewModel.pinUpNote() },
                deleteNote = {
                    viewModel.deleteNote()
                    navController.popBackStack()
                },
                changeColor = { color -> viewModel.changeColor(color) }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SuccessScreen(
    note: Note = mockNote,
    colors: List<String> = mockColorList,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (String) -> Unit = {},
    saveText: (String, String) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            NoteTopBar(
                note = note,
                colors = colors,
                onBackClick = onBackClick,
                changeColor = changeColor,
                pinUpNote = pinUpNote,
                deleteNote = deleteNote
            )
        },
        content = { padding ->
            NoteContent(
                padding = padding,
                note = note,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                saveText = saveText
            )
        },
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    note: Note = mockNote,
    colors: List<String> = mockColorList,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (String) -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
    var showColor by remember { mutableStateOf(false) }
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
            IconButton(onClick = { showColor = !showColor }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_color_lens),
                    contentDescription = "More icon",
                    tint = YellowDark
                )
            }

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
                        if (note.isPinned) {
                            MenuItem(R.drawable.ic_unpin, stringResource(R.string.unpin))
                        } else {
                            MenuItem(R.drawable.ic_pin, stringResource(R.string.pin))
                        }
                    },
                    onClick = {
                        showMenu = false
                        pinUpNote()
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_delete_outline,
                            stringResource(R.string.delete),
                            Red,
                            Red
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
                ChangeColorMenu(colors, changeColor)
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
    colors: List<String> = mockColorList,
    changeColor: (String) -> Unit = {},
) {
    for (i in 0 until colors.size.div(4)) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            for (j in 0 until 4) {
                ColorItem(item = colors[i * 4 + j], changeColor)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorItem(item: String = "", changeColor: (String) -> Unit = {}) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(36.dp)
            .combinedClickable(
                onClick = { changeColor(item) },
                onLongClick = { }
            )
    ) {
        Box(
            modifier = Modifier
                .background(getColor(item))
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    saveText: (String, String) -> Unit = { _, _ -> },
) {
    with(sharedTransitionScope) {
        val color = getColor(note.color)

        var titleTextField by remember(note.id) { mutableStateOf(TextFieldValue(note.title)) }
        var contentTextField by remember(note.id) { mutableStateOf(TextFieldValue(note.content)) }

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
                            saveText(titleTextField.text, contentTextField.text)
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
                        saveText(titleTextField.text, contentTextField.text)
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
