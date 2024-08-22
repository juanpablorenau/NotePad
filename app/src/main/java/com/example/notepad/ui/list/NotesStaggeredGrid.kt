package com.example.notepad.ui.list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
    checkNote: (id: String) -> Unit = {},
    swipeNotes: (oldIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    navigate: (String) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    var list = notes
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
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(list, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderableLazyStaggeredGridState, item.id) {
                val interactionSource = remember { MutableInteractionSource() }

                val route = AppScreens.NoteDetailScreen.route.plus("/${item.id}/0")
                val color = getColor(if (isDarkTheme) item.darkColor else item.lightColor)

                with(sharedTransitionScope) {
                    Card(
                        modifier = Modifier
                            .clickable { if(notes.none { it.isChecked }) navigate(route) else checkNote(item.id) }
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
                                onDragStarted = { checkNote(item.id) },
                                onDragStopped = {},
                                interactionSource = interactionSource,
                            ),
                        border = BorderStroke(
                            2.dp,
                            if (item.isChecked) MaterialTheme.colorScheme.secondary else Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = item.content,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 8
                            )
                        }
                    }
                }
            }
        }
    }
}