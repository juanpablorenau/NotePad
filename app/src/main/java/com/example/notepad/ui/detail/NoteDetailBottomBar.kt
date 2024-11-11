package com.example.notepad.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.enums.FormatType
import com.example.model.enums.ParagraphType
import com.example.model.enums.TextColor
import com.example.model.enums.TypeText
import com.example.notepad.R
import com.example.notepad.components.DisplayText
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockBodyFormat
import com.example.notepad.utils.mockNote

@Composable
fun NoteDetailBottomBar(
    isDarkTheme: Boolean = false,
    note: Note = mockNote,
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
    addTable: () -> Unit = {},
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val changeBottomSheetState = { value: Boolean -> showBottomSheet.value = value }
    val formatText = note.getFocusedItem()?.getFormatTextWithSameIndexes() ?: FormatText()

    if (showBottomSheet.value) {
        TextFormatComponent(
            formatText = formatText,
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
        DisplayText(description = R.string.text_format) {
            Icon(
                modifier = Modifier.clickable {
                    changeBottomSheetState(true)
                    keyboardController?.hide()
                },
                painter = painterResource(id = R.drawable.ic_border_color),
                contentDescription = "text format icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DisplayText(description = R.string.add_text) {
            Icon(
                modifier = Modifier.clickable { addTextField() },
                painter = painterResource(id = R.drawable.ic_text_fields),
                contentDescription = "text fields icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DisplayText(description = R.string.add_checkbox) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { addCheckBox(null) },
                painter = painterResource(id = R.drawable.ic_check_list),
                contentDescription = "check box icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DisplayText(description = R.string.add_table) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { addTable() },
                painter = painterResource(id = R.drawable.ic_grid),
                contentDescription = "grid icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DisplayText(description = R.string.add_image) {
            Icon(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = "Image icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormatComponent(
    formatText: FormatText = mockBodyFormat,
    isDarkTheme: Boolean = false,
    changeBottomSheetState: (Boolean) -> Unit = {},
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
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
            TextFormatContent(formatText, isDarkTheme, applyFormat)
        }
    }
}

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
    formatText: FormatText = mockBodyFormat,
    isDarkTheme: Boolean = false,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        TypeTextsSelector(formatText.typeText, applyFormat)
        FormatTextsSelector(formatText, applyFormat)
        ParagraphsSelectorAndTextColor(formatText, isDarkTheme, applyFormat)
    }

}

@Composable
fun TypeTextsSelector(
    typeText: TypeText = TypeText.HEADER,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    val typeTexts = remember { TypeText.entries }
    val selectedIndex =
        remember(typeText) { mutableIntStateOf(typeTexts.indexOfFirst { it == typeText }) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(typeTexts) { index, type ->
            TypeTextsItem(
                index = index,
                typeText = type,
                selectedIndex = selectedIndex,
                applyFormat = applyFormat
            )
        }
    }
}

@Composable
fun TypeTextsItem(
    index: Int = -1,
    typeText: TypeText = TypeText.HEADER,
    selectedIndex: MutableState<Int> = mutableIntStateOf(-1),
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    Card(
        modifier = Modifier
            .height(40.dp)
            .clickable {
                selectedIndex.value = index
                applyFormat(FormatType.TYPE_TEXT, FormatText(typeText))
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
                text = when (typeText) {
                    TypeText.TITLE -> stringResource(id = R.string.title)
                    TypeText.HEADER -> stringResource(id = R.string.header)
                    TypeText.SUBTITLE -> stringResource(id = R.string.subtitle)
                    TypeText.BODY -> stringResource(id = R.string.body)
                },
                fontSize = typeText.fontSize.sp,
                fontWeight = if (typeText.isBold) FontWeight.Bold else FontWeight.Normal,
                color =
                if (selectedIndex.value == index) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun FormatTextsSelector(
    formatText: FormatText = mockBodyFormat,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    with(formatText) {
        val selectedIndexes = remember(this) {
            mutableStateListOf(isBold, isItalic, isUnderline, isLineThrough)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ) {
            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable {
                        selectedIndexes[0] = !selectedIndexes[0]
                        applyFormat(FormatType.BOLD, FormatText(isBold = selectedIndexes[0]))
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
                        applyFormat(FormatType.ITALIC, FormatText(isItalic = selectedIndexes[1]))
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
                        applyFormat(
                            FormatType.UNDERLINE,
                            FormatText(isUnderline = selectedIndexes[2])
                        )
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
                        applyFormat(
                            FormatType.LINE_THROUGH,
                            FormatText(isLineThrough = selectedIndexes[3])
                        )
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

@Composable
fun ParagraphsSelectorAndTextColor(
    formatText: FormatText = mockBodyFormat,
    isDarkTheme: Boolean = false,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
) {
    val paragraphType = remember { mutableStateOf(formatText.paragraphType) }

    val showColorSelector = remember { mutableStateOf(false) }
    val color =
        getColor(if (isDarkTheme) formatText.color.darkColor else formatText.color.lightColor)

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
                            applyFormat(
                                FormatType.PARAGRAPH_TYPE,
                                FormatText(paragraphType = ParagraphType.LEFT)
                            )
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
                            applyFormat(
                                FormatType.PARAGRAPH_TYPE,
                                FormatText(paragraphType = ParagraphType.JUSTIFY)
                            )
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
                            applyFormat(
                                FormatType.PARAGRAPH_TYPE,
                                FormatText(paragraphType = ParagraphType.CENTER)
                            )
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
                            applyFormat(
                                FormatType.PARAGRAPH_TYPE,
                                FormatText(paragraphType = ParagraphType.RIGHT)
                            )
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

        if (showColorSelector.value) TextColorSelector(isDarkTheme, applyFormat)
    }
}

@Composable
fun TextColorSelector(
    isDarkTheme: Boolean = false,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
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
                item = item,
                applyFormat = applyFormat,
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun TextColorItem(
    item: TextColor = TextColor.BASIC,
    applyFormat: (FormatType, FormatText) -> Unit = { _, _ -> },
    isDarkTheme: Boolean = false,
) {
    val color = remember(item, isDarkTheme) {
        getColor(if (isDarkTheme) item.darkColor else item.lightColor)
    }

    Card(
        modifier = Modifier
            .size(32.dp)
            .padding(1.dp)
            .clickable { applyFormat(FormatType.TEXT_COLOR, FormatText(color = item)) },
        shape = RoundedCornerShape(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        )
    }
}

