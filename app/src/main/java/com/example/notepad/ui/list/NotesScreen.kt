package com.example.notepad.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.model.entities.Note
import com.example.notepad.R
import com.example.notepad.components.Dialog
import com.example.notepad.components.MenuItem
import com.example.notepad.navigation.AppScreens
import com.example.notepad.theme.LightGray1
import com.example.notepad.theme.YellowDark
import com.example.notepad.utils.getColor
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNoteList
import kotlin.math.roundToInt

@Composable
fun NotesScreen(navController: NavHostController) {
    val viewModel = LocalContext.current.getViewModel<NotesViewModel>()
    val uiState: NotesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is NotesUiState.Loading -> Unit
        is NotesUiState.Error -> Unit
        is NotesUiState.Success ->
            SuccessScreen(
                notes = state.notes,
                itemsView = state.itemsView,
                onSearch = { searchText -> viewModel.searchNotes(searchText) },
                getNotes = { viewModel.getNotes() },
                onCardClick = { index -> viewModel.checkNote(index) },
                onCardLongClick = { index, offset -> viewModel.saveOffSetInNotes(index, offset) },
                deleteNotes = { viewModel.deleteCheckedNotes() },
                pinUpNotes = { viewModel.pinUpCheckedNotes() },
                changeItemsView = { viewModel.changeItemsView() },
                resetPositions = { viewModel.resetPositions() },
                selectAllNotes = { viewModel.selectAllNotes() },
                navigate = { route -> navController.navigate(route) }
            )
    }
}

@Preview
@Composable
fun SuccessScreen(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    onSearch: (String) -> Unit = {},
    getNotes: () -> Unit = {},
    onCardClick: (index: Int) -> Unit = {},
    onCardLongClick: (index: Int, offset: IntOffset) -> Unit = { _, _ -> },
    deleteNotes: () -> Unit = {},
    pinUpNotes: () -> Unit = {},
    changeItemsView: () -> Unit = {},
    resetPositions: () -> Unit = {},
    selectAllNotes: () -> Unit = {},
    navigate: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            NotesTopBar(
                notes = notes,
                itemsView = itemsView,
                deleteNotes = deleteNotes,
                pinUpNotes = pinUpNotes,
                changeItemsView = changeItemsView,
                resetPositions = resetPositions,
                selectAllNotes = selectAllNotes
            )
        },
        content = { padding ->
            NotesContent(
                padding = padding,
                notes = notes,
                itemsView = itemsView,
                onSearch = onSearch,
                getNotes = getNotes,
                onCardClick = onCardClick,
                onCardLongClick = onCardLongClick,
                navigate = navigate
            )
        },
        floatingActionButton = { AddNoteButton() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    deleteNotes: () -> Unit = {},
    pinUpNotes: () -> Unit = {},
    changeItemsView: () -> Unit = {},
    resetPositions: () -> Unit = {},
    selectAllNotes: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
    var deleteButtonClicked by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier.padding(end = 12.dp),
        title = {
            Text(
                text = stringResource(R.string.notes_title),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            IconButton(onClick = { changeItemsView() }) {
                Icon(
                    painter = painterResource(id = if (itemsView == 1) R.drawable.ic_grid_view else R.drawable.ic_list),
                    contentDescription = "Grid icon",
                    tint = YellowDark
                )
            }

            IconButton(onClick = { resetPositions() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = "Reset icon",
                    tint = YellowDark
                )
            }

            if (notes.any { it.isChecked }) {
                Text(text = notes.count { it.isChecked }.toString(), color = YellowDark)

                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "More icon",
                        tint = YellowDark
                    )
                }
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { MenuItem(R.drawable.ic_check_circle, stringResource(R.string.select_all)) },
                    onClick = { selectAllNotes() },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(R.drawable.ic_pin, stringResource(R.string.pin))
                    },
                    onClick = {
                        showMenu = false
                        pinUpNotes()
                    },
                )

                DropdownMenuItem(
                    text = { MenuItem(R.drawable.ic_delete_outline, stringResource(R.string.delete),) },
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
                deleteNotes()
            },
            noAction = { deleteButtonClicked = false })
    }
}

@Composable
fun NotesContent(
    padding: PaddingValues = PaddingValues(),
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    onSearch: (String) -> Unit = {},
    getNotes: () -> Unit = {},
    onCardClick: (index: Int) -> Unit = {},
    onCardLongClick: (index: Int, offset: IntOffset) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(
            top = padding.calculateTopPadding(),
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        SearchNote(onSearch = onSearch, getNotes = getNotes)
        Spacer(modifier = Modifier.height(16.dp))
        NotesList(notes, itemsView, onCardClick, onCardLongClick, navigate)
    }
}

@Composable
private fun SearchNote(onSearch: (String) -> Unit, getNotes: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = LightGray1,
            unfocusedContainerColor = LightGray1
        ),
        placeholder = {
            Text(
                modifier = Modifier.fillMaxHeight(),
                text = stringResource(R.string.search),
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = Color.Gray
            )
        },
        value = searchText,
        onValueChange = { newText -> searchText = newText },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            onSearch(searchText.lowercase())
        }),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (searchText.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = Color.Gray,
                    modifier = Modifier.clickable(onClick = {
                        searchText = ""
                        keyboardController?.hide()
                        getNotes()
                    })
                )
            }
        })

}

@Composable
fun NotesList(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    onCardClick: (index: Int) -> Unit = {},
    onCardLongClick: (index: Int, offset: IntOffset) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(itemsView),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            itemsIndexed(notes) { index, note ->
                ItemNote(note, index, onCardClick, onCardLongClick, navigate)
            }
        })
}

@Composable
fun ItemNote(
    note: Note,
    index: Int,
    onCardClick: (index: Int) -> Unit = {},
    onCardLongClick: (index: Int, offset: IntOffset) -> Unit,
    navigate: (String) -> Unit = {},
) {
    val color = getColor(note.color)
    val route = AppScreens.NoteDetailScreen.route.plus("/" + note.id)

    var isDragging by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    offsetX = note.offsetX
    offsetY = note.offsetY

    Card(
        shape = Shapes().medium,
        border = BorderStroke(2.dp, if (note.isChecked) Color.Gray else Color.Transparent),
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .clickable { onCardClick(index) }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        onCardLongClick(index, IntOffset(offsetX.roundToInt(), offsetY.roundToInt()))
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(if (isDragging) Color.LightGray else color)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (note.isPinned) {
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd),
                        painter = painterResource(id = R.drawable.ic_pin),
                        contentDescription = "Pinned icon",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = note.content,
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Icon(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(12.dp)
                    .clickable { navigate(route) },
                painter = painterResource(id = R.drawable.ic_open_in_full),
                tint = Color.Black,
                contentDescription = "Fullscreen Icon "
            )
        }
    }
}

@Composable
fun AddNoteButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        containerColor = Color.White,
        onClick = { onClick() }
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add_icon",
            tint = YellowDark
        )
    }
}