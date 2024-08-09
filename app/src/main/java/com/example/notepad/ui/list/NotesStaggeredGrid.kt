package com.example.notepad.ui.list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.notepad.R
import com.example.notepad.components.staggeredgrid.ReorderableItem
import com.example.notepad.components.staggeredgrid.rememberReorderableLazyStaggeredGridState
import com.example.notepad.navigation.AppScreens
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockNoteList

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun NotesStaggeredGrid(
    notes: List<Note> = mockNoteList,
    itemsView: Int = 2,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    checkNote: (index: Int) -> Unit = {},
    swipeNotes: (oldIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
) {

    var list by remember { mutableStateOf(notes) }
    list = notes
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val reorderableLazyStaggeredGridState =
        rememberReorderableLazyStaggeredGridState(lazyStaggeredGridState) { from, to ->
            list = list.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
            swipeNotes(from.index, to.index)
        }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(itemsView),
        modifier = Modifier.fillMaxSize(),
        state = lazyStaggeredGridState,
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(list, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderableLazyStaggeredGridState, item.id) {
                val interactionSource = remember { MutableInteractionSource() }
                val route = AppScreens.NoteDetailScreen.route.plus("/" + item.id)

                with(sharedTransitionScope) {
                    Card(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = { navigate(route) }, onLongClick = { checkNote(index) }
                            )
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = item.id),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .semantics {
                                customActions = listOf(
                                    CustomAccessibilityAction(
                                        label = "Move Before",
                                        action = {
                                            if (index > 0) {
                                                list = list
                                                    .toMutableList()
                                                    .apply {
                                                        add(index - 1, removeAt(index))
                                                    }
                                                true
                                            } else {
                                                false
                                            }
                                        }
                                    ),
                                    CustomAccessibilityAction(
                                        label = "Move After",
                                        action = {
                                            if (index < list.size - 1) {
                                                list = list.toMutableList()
                                                    .apply { add(index + 1, removeAt(index)) }
                                                true
                                            } else {
                                                false
                                            }
                                        }
                                    ),
                                )
                            }
                            .longPressDraggableHandle(
                                onDragStarted = {},
                                onDragStopped = {},
                                interactionSource = interactionSource,
                            ),
                        border = BorderStroke(
                            2.dp,
                            if (item.isChecked) Color.Gray else Color.Transparent
                        ),
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            val color = getColor(item.color)
                            Column(
                                modifier = Modifier
                                    .background(color)
                                    .padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        modifier = Modifier.padding(end = 12.dp),
                                        text = item.title,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Start,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 2,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    if (item.isPinned) {
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
                                    text = item.content,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Start,
                                    color = Color.DarkGray,
                                    maxLines = 8
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}