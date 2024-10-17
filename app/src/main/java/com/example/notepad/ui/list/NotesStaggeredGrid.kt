package com.example.notepad.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteItemType
import com.example.notepad.R
import com.example.notepad.components.staggeredgrid.ReorderableItem
import com.example.notepad.components.staggeredgrid.rememberReorderableLazyStaggeredGridState
import com.example.notepad.navigation.AppScreens
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockNoteItems
import com.example.notepad.utils.mockNoteList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesStaggeredGrid(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    checkNote: (id: String) -> Unit = {},
    swipeNotes: (oldIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val gridState = rememberLazyStaggeredGridState()
    val reorderGridState = rememberReorderableLazyStaggeredGridState(gridState) { from, to ->
        swipeNotes(from.index, to.index)
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(itemsView),
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(notes, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderGridState, item.id) {
                val interactionSource = remember { MutableInteractionSource() }

                val route = AppScreens.NoteDetailScreen.route.plus("/${item.id}/0")
                val color = getColor(if (isDarkTheme) item.darkNoteColor else item.lightNoteColor)

                Card(
                    modifier = Modifier
                        .clickable {
                            if (notes.none { it.isChecked }) navigate(route)
                            else checkNote(item.id)
                        }
                        .semantics {
                            customActions = listOf(
                                CustomAccessibilityAction(
                                    label = "Move Before",
                                    action = {
                                        if (index > 0) swipeNotes(index - 1, index)
                                        index > 0
                                    }
                                ),
                                CustomAccessibilityAction(
                                    label = "Move After",
                                    action = {
                                        if (index < notes.size - 1) {
                                            swipeNotes(index + 1, index)
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                ),
                            )
                        }
                        .longPressDraggableHandle(
                            onDragStarted = { checkNote(item.id) },
                            onDragStopped = {},
                            interactionSource = interactionSource,
                        ),
                    border = BorderStroke(
                        2.dp,
                        if (item.isChecked) MaterialTheme.colorScheme.secondary
                        else Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(16.dp)
                    ) {
                        NoteHeader(item)
                        Spacer(modifier = Modifier.height(16.dp))
                        NoteBody(item.items.take(3), isDarkTheme)
                    }
                }
            }
        }
    }
}

@Composable
fun NoteHeader(item: Note) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(end = 12.dp),
            text = item.title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary,
        )
        if (item.isPinned) {
            Icon(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = "Pinned icon",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun NoteBody(notesItems: List<NoteItem> = mockNoteItems, isDarkTheme: Boolean = false) {
    notesItems.forEachIndexed { index, item ->
        val isPreviousItemTable = notesItems.getOrNull(index - 1)?.isTable() ?: false

        when (item.type) {
            NoteItemType.TEXT -> TextFieldItem(item, isDarkTheme)
            NoteItemType.CHECK_BOX -> CheckBoxItem(item, isDarkTheme)
            NoteItemType.TABLE -> TableItem(item, isDarkTheme, isPreviousItemTable)
        }
    }
}