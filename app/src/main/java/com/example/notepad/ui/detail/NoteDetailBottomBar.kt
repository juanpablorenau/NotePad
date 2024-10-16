package com.example.notepad.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.*
import com.example.notepad.R
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockNote
import com.example.notepad.utils.mockTextItem


@Preview(showBackground = true)
@Composable
fun NoteDetailBottomBar(
    isDarkTheme: Boolean = false,
    note: Note = mockNote,
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
    addTable: () -> Unit = {},
    applyFormat: (FormatText) -> Unit = {},
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val changeBottomSheetState = { value: Boolean -> showBottomSheet.value = value }
    val noteItem =  note.items.find { it.isFocused } ?: NoteItem("", "", 0)

    if (showBottomSheet.value) {
        TextFormatComponent(
            noteItem = noteItem,
            isDarkTheme = isDarkTheme,
            changeBottomSheetState = changeBottomSheetState,
            applyFormat = applyFormat
        )
    } else {
        BottomOptions(
            changeBottomSheetState = changeBottomSheetState,
            addTextField = addTextField,
            addCheckBox = addCheckBox,
            addTable = addTable
        )
    }
}

@Composable
fun BottomOptions(
    changeBottomSheetState: (Boolean) -> Unit = {},
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
    addTable: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable {
                changeBottomSheetState(true)
                keyboardController?.hide()
            },
            painter = painterResource(id = R.drawable.ic_border_color),
            contentDescription = "text format icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            modifier = Modifier.clickable { addTextField() },
            painter = painterResource(id = R.drawable.ic_text_fields),
            contentDescription = "text fields icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .clickable { addCheckBox(null) },
            painter = painterResource(id = R.drawable.ic_check_list),
            contentDescription = "check box icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { addTable() },
            painter = painterResource(id = R.drawable.ic_grid),
            contentDescription = "grid icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = "Image icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormatComponent(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    changeBottomSheetState: (Boolean) -> Unit = {},
    applyFormat: (FormatText) -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(
            topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
        ) {
            TextFormatHeader(changeBottomSheetState)
            TextFormatContent(noteItem, isDarkTheme, applyFormat)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormatHeader(
    changeBottomSheetState: (Boolean) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.text_format),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            modifier = Modifier.clickable {
                changeBottomSheetState(false)
                keyboardController?.show()
            },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close icon",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormatContent(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    applyFormat: (FormatText) -> Unit = {},
) {
    Spacer(modifier = Modifier.height(24.dp))
    TypeTextsSelector(noteItem, applyFormat)
    Spacer(modifier = Modifier.height(12.dp))
    FormatTextsSelector(noteItem, applyFormat)
    Spacer(modifier = Modifier.height(12.dp))
    ParagraphsSelectorAndTextColor(noteItem, isDarkTheme, applyFormat)
    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true)
@Composable
fun TypeTextsSelector(
    noteItem: NoteItem = mockTextItem,
    applyFormat: (FormatText) -> Unit = {},
) {
    val formatTexts = remember {
        listOf(
            FormatText("0", "", TypeText.TITLE, 24, true),
            FormatText("1", "", TypeText.HEADER, 20, false),
            FormatText("2", "", TypeText.SUBTITLE, 16, true),
            FormatText("3", "", TypeText.BODY, 16, false)
        )
    }
    val selectedIndex = remember { mutableIntStateOf(-1) }
    selectedIndex.intValue =
        formatTexts.indexOfFirst { it.typeText == noteItem.formatText.typeText }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(formatTexts) { index, formatText ->
            TypeTextsItem(
                noteItem = noteItem,
                index = index,
                formatText = formatText,
                selectedIndex = selectedIndex,
                applyFormat = applyFormat
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TypeTextsItem(
    noteItem: NoteItem = mockTextItem,
    index: Int = -1,
    formatText: FormatText = FormatText(""),
    selectedIndex: MutableState<Int> = mutableIntStateOf(-1),
    applyFormat: (FormatText) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .height(40.dp)
            .clickable {
                selectedIndex.value = index
                applyFormat(
                    noteItem.formatText.copy(
                        typeText = formatText.typeText,
                        fontSize = formatText.fontSize,
                        isBold = formatText.isBold
                    )
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (selectedIndex.value == index) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.background
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = when (formatText.typeText) {
                    TypeText.TITLE -> stringResource(id = R.string.title)
                    TypeText.HEADER -> stringResource(id = R.string.header)
                    TypeText.SUBTITLE -> stringResource(id = R.string.subtitle)
                    TypeText.BODY -> stringResource(id = R.string.body)
                },
                fontSize = formatText.fontSize.sp,
                fontWeight = if (formatText.isBold) FontWeight.Bold else FontWeight.Normal,
                color =
                if (selectedIndex.value == index) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormatTextsSelector(
    noteItem: NoteItem = mockTextItem,
    applyFormat: (FormatText) -> Unit = {},
) {
    with(noteItem.formatText) {
        val selectedIndexes = remember(this) {
            mutableStateListOf(isBold, isItalic, isUnderline, isLineThrough)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable {
                        selectedIndexes[0] = !selectedIndexes[0]
                        applyFormat(copy(isBold = selectedIndexes[0]))
                    },
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 0.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndexes[0]) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                        text = "B",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color =
                        if (selectedIndexes[0]) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable {
                        selectedIndexes[1] = !selectedIndexes[1]
                        applyFormat(copy(isItalic = selectedIndexes[1]))
                    },
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndexes[1]) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                        text = "I",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color =
                        if (selectedIndexes[1]) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable {
                        selectedIndexes[2] = !selectedIndexes[2]
                        applyFormat(copy(isUnderline = selectedIndexes[2]))
                    },
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndexes[2]) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                        text = "U",
                        fontSize = 16.sp,
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        color =
                        if (selectedIndexes[2]) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable {
                        selectedIndexes[3] = !selectedIndexes[3]
                        applyFormat(copy(isLineThrough = selectedIndexes[3]))
                    },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 12.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 12.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndexes[3]) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                        text = "S",
                        fontSize = 16.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color =
                        if (selectedIndexes[3]) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParagraphsSelectorAndTextColor(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    applyFormat: (FormatText) -> Unit = {},
) {
    val paragraphType = remember { mutableStateOf(noteItem.formatText.paragraphType) }

    val showColorSelector = remember { mutableStateOf(false) }
    val color =
        getColor(
            if (isDarkTheme) noteItem.formatText.textDarkColor
            else noteItem.formatText.textLightColor
        )

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable { showColorSelector.value = !showColorSelector.value },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_text_format),
                        contentDescription = "text color icon",
                        tint = color,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier.weight(4f)
            ) {
                Card(
                    modifier = Modifier
                        .height(32.dp)
                        .weight(1f)
                        .clickable {
                            paragraphType.value = ParagraphType.LEFT
                            applyFormat(noteItem.formatText.copy(paragraphType = ParagraphType.LEFT))
                        },
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 0.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        if (paragraphType.value == ParagraphType.LEFT) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_format_align_left),
                            contentDescription = "text color icon",
                            tint =
                            if (paragraphType.value == ParagraphType.LEFT) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(1.dp))

                Card(
                    modifier = Modifier
                        .height(32.dp)
                        .weight(1f)
                        .clickable {
                            paragraphType.value = ParagraphType.JUSTIFY
                            applyFormat(noteItem.formatText.copy(paragraphType = ParagraphType.JUSTIFY))
                        },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        if (paragraphType.value == ParagraphType.JUSTIFY) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_format_align_justify),
                            contentDescription = "text color icon",
                            tint =
                            if (paragraphType.value == ParagraphType.JUSTIFY) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(1.dp))

                Card(
                    modifier = Modifier
                        .height(32.dp)
                        .weight(1f)
                        .clickable {
                            paragraphType.value = ParagraphType.CENTER
                            applyFormat(noteItem.formatText.copy(paragraphType = ParagraphType.CENTER))
                        },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        if (paragraphType.value == ParagraphType.CENTER) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_format_align_center),
                            contentDescription = "text color icon",
                            tint =
                            if (paragraphType.value == ParagraphType.CENTER) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(1.dp))

                Card(
                    modifier = Modifier
                        .height(32.dp)
                        .weight(1f)
                        .clickable {
                            paragraphType.value = ParagraphType.RIGHT
                            applyFormat(noteItem.formatText.copy(paragraphType = ParagraphType.RIGHT))
                        },
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 12.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        if (paragraphType.value == ParagraphType.RIGHT) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_format_align_right),
                            contentDescription = "text color icon",
                            tint =
                            if (paragraphType.value == ParagraphType.RIGHT) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }

        if (showColorSelector.value) TextColorSelector(noteItem, isDarkTheme, applyFormat)
    }
}

@Preview(showBackground = true)
@Composable
fun TextColorSelector(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    applyFormat: (FormatText) -> Unit = {},
) {
    val colors = remember { mutableStateOf(TextColor.entries) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(colors.value) { item ->
            TextColorItem(
                noteItem = noteItem,
                item = item,
                applyFormat = applyFormat,
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun TextColorItem(
    noteItem: NoteItem = mockTextItem,
    item: TextColor = TextColor.BASIC,
    applyFormat: (FormatText) -> Unit = {},
    isDarkTheme: Boolean = false,
) {
    val color = getColor(if (isDarkTheme) item.darkColor else item.lightColor)

    Card(
        modifier = Modifier
            .size(32.dp)
            .padding(1.dp)
            .clickable {
                applyFormat(
                    noteItem.formatText.copy(
                        textLightColor = item.lightColor, textDarkColor = item.darkColor
                    )
                )
            },
        shape = RoundedCornerShape(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        )
    }
}

