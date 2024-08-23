package com.example.notepad.ui.list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.notepad.navigation.AppScreens
import com.example.notepad.utils.getViewModel
import com.example.notepad.utils.mockNoteList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotesScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    openDrawer: () -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val viewModel = LocalContext.current.getViewModel<NotesViewModel>()
    val uiState: NotesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.getNotes()
        onPauseOrDispose { viewModel.updateNotes() }
    }

    when (val state = uiState) {
        is NotesUiState.Loading -> LoadingScreen()
        is NotesUiState.Error -> ErrorScreen { navController.popBackStack() }
        is NotesUiState.Success ->
            SuccessScreen(
                notes = state.notes,
                itemsView = state.itemsView,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onSearch = { searchText -> viewModel.searchNotes(searchText) },
                restoreNotes = { notes -> viewModel.restoreNotes(notes) },
                checkNote = { id -> viewModel.checkNote(id) },
                swipeNotes = { oldIndex, newIndex -> viewModel.swipeNotes(oldIndex, newIndex) },
                deleteNotes = { viewModel.deleteNotes() },
                pinUpNotes = { viewModel.pinUpCheckedNotes() },
                changeItemsView = { viewModel.changeItemsView() },
                selectAllNotes = { select -> viewModel.selectAllNotes(select) },
                navigate = { route -> navController.navigate(route) },
                openDrawer = { openDrawer() },
                isDarkTheme = isDarkTheme
            )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SuccessScreen(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onSearch: (String) -> Unit = {},
    restoreNotes: (List<Note>) -> Unit = {},
    checkNote: (id: String) -> Unit = {},
    swipeNotes: (oldIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    deleteNotes: () -> Unit = {},
    pinUpNotes: () -> Unit = {},
    changeItemsView: () -> Unit = {},
    selectAllNotes: (Boolean) -> Unit = {},
    navigate: (String) -> Unit = {},
    openDrawer: () -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    var isSearchBarVisible by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            NotesTopBar(
                notes = notes,
                itemsView = itemsView,
                deleteNotes = deleteNotes,
                pinUpNotes = pinUpNotes,
                changeItemsView = changeItemsView,
                selectAllNotes = selectAllNotes,
                setSearchBarVisible = { isSearchBarVisible = !isSearchBarVisible },
                openDrawer = { openDrawer() }
            )
        },
        content = { padding ->
            NotesContent(
                padding = padding,
                notes = notes,
                itemsView = itemsView,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onSearch = onSearch,
                restoreNotes = restoreNotes,
                checkNote = checkNote,
                swipeNotes = swipeNotes,
                navigate = navigate,
                getSearchBarVisible = { isSearchBarVisible },
                isDarkTheme = isDarkTheme
            )
        },
        floatingActionButton = { AddNoteButton(navigate, notes.size) }
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    deleteNotes: () -> Unit = {},
    pinUpNotes: () -> Unit = {},
    changeItemsView: () -> Unit = {},
    selectAllNotes: (Boolean) -> Unit = {},
    setSearchBarVisible: () -> Unit = {},
    openDrawer: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
    var deleteButtonClicked by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        navigationIcon = {
            IconButton(onClick = { openDrawer() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.notes_title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            if (notes.none { it.isChecked }) {
                IconButton(onClick = { changeItemsView() }) {
                    Icon(
                        painter = painterResource(id = if (itemsView == 1) R.drawable.ic_grid_view else R.drawable.ic_list),
                        contentDescription = "Grid icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { setSearchBarVisible() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Text(
                    text = notes.count { it.isChecked }.toString(),
                    color = MaterialTheme.colorScheme.primary
                )

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
                        MenuItem(
                            R.drawable.ic_check_circle,
                            stringResource(R.string.select_all),
                            iconColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.secondary
                        )
                    },
                    onClick = { selectAllNotes(true) },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_check_circle_outline,
                            stringResource(R.string.unselect_all),
                            iconColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.secondary
                        )
                    },
                    onClick = {
                        showMenu = false
                        selectAllNotes(false)
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_pin, stringResource(R.string.pin),
                            iconColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.secondary
                        )
                    },
                    onClick = {
                        showMenu = false
                        pinUpNotes()
                    },
                )

                DropdownMenuItem(
                    text = {
                        MenuItem(
                            R.drawable.ic_delete_outline,
                            stringResource(R.string.delete),
                            iconColor = Red,
                            textColor = Red
                        )
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
                deleteNotes()
            },
            noAction = { deleteButtonClicked = false })
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotesContent(
    padding: PaddingValues = PaddingValues(),
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onSearch: (String) -> Unit = {},
    restoreNotes: (List<Note>) -> Unit = {},
    checkNote: (id: String) -> Unit = {},
    swipeNotes: (oldIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
    getSearchBarVisible: () -> Boolean = { false },
    isDarkTheme: Boolean = false,
) {
    Column(
        modifier = Modifier.padding(
            top = padding.calculateTopPadding(),
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        SearchNote(
            notes = notes,
            onSearch = onSearch,
            restoreNotes = restoreNotes,
            getSearchBarVisible = getSearchBarVisible
        )
        NotesStaggeredGrid(
            notes = notes,
            itemsView = itemsView,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            checkNote = checkNote,
            swipeNotes = swipeNotes,
            navigate = navigate,
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
private fun SearchNote(
    notes: List<Note> = mockNoteList,
    onSearch: (String) -> Unit = {},
    restoreNotes: (List<Note>) -> Unit = {},
    getSearchBarVisible: () -> Boolean = { false },
) {
    val focusRequester = remember { FocusRequester() }
    val originalNotes by remember { mutableStateOf(notes) }
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (!getSearchBarVisible()) return
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
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
        onValueChange = { newText ->
            searchText = newText
            if (searchText.isBlank()) restoreNotes(originalNotes)
            else onSearch(searchText.lowercase())
        },
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
                        restoreNotes(originalNotes)
                    })
                )
            }
        })

    Spacer(modifier = Modifier.height(16.dp))

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun AddNoteButton(onClick: (String) -> Unit = {}, size: Int) {
    val route = AppScreens.NoteDetailScreen.route.plus("/new_element/$size")

    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.tertiary,
        onClick = { onClick(route) }
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add_icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}