package com.example.notepad.ui.detail

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
import com.example.notepad.utils.getColor
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNote
import com.example.model.entities.Color as AppColor

@Composable
fun NoteDetailScreen(
    navController: NavHostController,
    noteId: String,
    index: Int,
    isDarkTheme: Boolean = false,
) {
    val viewModel = LocalContext.current.getViewModel<NoteDetailViewModel>()
    val uiState: NoteDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(noteId) {
        viewModel.manageNote(noteId, index)
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
                onBackClick = { navController.popBackStack() },
                saveText = { title, content -> viewModel.saveText(title, content) },
                pinUpNote = { viewModel.pinUpNote() },
                deleteNote = {
                    viewModel.deleteNote()
                    navController.popBackStack()
                },
                changeColor = { color -> viewModel.changeColor(color) },
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun SuccessScreen(
    note: Note = mockNote,
    colors: List<AppColor> = AppColor.entries,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (AppColor) -> Unit = {},
    saveText: (String, String) -> Unit = { _, _ -> },
    isDarkTheme: Boolean = false,
) {
    Scaffold(
        topBar = {
            NoteDetailTopBar(
                note = note,
                colors = colors,
                onBackClick = onBackClick,
                changeColor = changeColor,
                pinUpNote = pinUpNote,
                deleteNote = deleteNote,
                isDarkTheme = isDarkTheme
            )
        },
        content = { padding ->
            NoteContent(
                padding = padding,
                note = note,
                saveText = saveText,
                isDarkTheme = isDarkTheme
            )
        },
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailTopBar(
    note: Note = mockNote,
    colors: List<AppColor> = AppColor.entries,
    onBackClick: () -> Unit = {},
    pinUpNote: () -> Unit = {},
    deleteNote: () -> Unit = {},
    changeColor: (AppColor) -> Unit = {},
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
            IconButton(onClick = { showColor = !showColor }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_color_lens),
                    contentDescription = "More icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "More icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        if (note.isPinned) {
                            MenuItem(
                                R.drawable.ic_unpin, stringResource(R.string.unpin),
                                iconColor = MaterialTheme.colorScheme.secondary,
                                textColor = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            MenuItem(
                                R.drawable.ic_pin,
                                stringResource(R.string.pin),
                                iconColor = MaterialTheme.colorScheme.secondary,
                                textColor = MaterialTheme.colorScheme.secondary
                            )
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
                ChangeColorMenu(colors, changeColor, isDarkTheme)
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
    colors: List<AppColor> = AppColor.entries,
    changeColor: (AppColor) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    for (i in 0 until colors.size.div(4)) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            for (j in 0 until 4) {
                ColorItem(item = colors[i * 4 + j], changeColor, isDarkTheme)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorItem(
    item: AppColor = AppColor.PALE_YELLOW,
    changeColor: (AppColor) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val color = getColor(if (isDarkTheme) item.darkColor else item.lightColor)

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
                .background(color)
                .fillMaxSize()
        )
    }
}

@Composable
fun NoteContent(
    padding: PaddingValues = PaddingValues(),
    note: Note = mockNote,
    saveText: (String, String) -> Unit = { _, _ -> },
    isDarkTheme: Boolean = false,
) {
    val color = getColor(if (isDarkTheme) note.darkColor else note.lightColor)

    var titleTextField by remember(note.id) { mutableStateOf(TextFieldValue(note.title)) }
    var contentTextField by remember(note.id) { mutableStateOf(TextFieldValue(note.content)) }

    Card(
        shape = Shapes().medium,
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding())
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
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
                        tint = MaterialTheme.colorScheme.secondary,
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
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
