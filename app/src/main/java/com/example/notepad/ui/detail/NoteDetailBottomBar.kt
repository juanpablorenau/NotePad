package com.example.notepad.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.entities.TypeText
import com.example.notepad.R


@Preview(showBackground = true)
@Composable
fun NoteDetailBottomBar(
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
) {
    val showBottomSheet = remember { mutableStateOf(false) }

    if (showBottomSheet.value) TextFormatComponent(showBottomSheet)
    else BottomOptions(showBottomSheet, addTextField, addCheckBox)
}

@Composable
fun BottomOptions(
    showBottomSheet: MutableState<Boolean> = mutableStateOf(true),
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
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
                showBottomSheet.value = true
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
            modifier = Modifier.size(24.dp),
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
    showBottomSheet: MutableState<Boolean> = mutableStateOf(true),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(
            topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
        ) {
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
                        showBottomSheet.value = false
                        keyboardController?.show()
                    },
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "close icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )

                TextFormatContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormatContent(){
    Spacer(modifier = Modifier.height(24.dp))

    TypeTextsSelector()

    Spacer(modifier = Modifier.height(18.dp))

    FormatTextsSelector()

    Spacer(modifier = Modifier.height(12.dp))

    ParagraphsSelectorAndTextColor()

    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true)
@Composable
fun TypeTextsSelector() {
    val selectedIndex = remember { mutableIntStateOf(-1) }

    val typeTexts = remember {
        listOf(
            TypeText("Title", 24, true),
            TypeText("Header", 20, false),
            TypeText("Subtitle", 16, true),
            TypeText("Body", 16, false)
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(typeTexts) { index, typeText ->
            TypeTextsItem(index, typeText, selectedIndex)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TypeTextsItem(
    index: Int = -1,
    typeText: TypeText = TypeText("Title", 24, true),
    selectedIndex: MutableState<Int> = mutableIntStateOf(-1),
) {
    Card(
        modifier = Modifier
            .height(40.dp)
            .clickable { selectedIndex.value = index },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (selectedIndex.value == index) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.background
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = typeText.text,
                fontSize = typeText.fontSize.sp,
                fontWeight = if (typeText.isBold) FontWeight.Bold else FontWeight.Normal,
                color =
                if (selectedIndex.value == index) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormatTextsSelector() {
    val selectedIndexes = remember { mutableStateListOf(false, false, false, false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier
                .height(32.dp)
                .weight(1f)
                .clickable { selectedIndexes[0] = !selectedIndexes[0] },
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
                .clickable { selectedIndexes[1] = !selectedIndexes[1] },
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
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
                .clickable { selectedIndexes[2] = !selectedIndexes[2] },
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
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
                .clickable { selectedIndexes[3] = !selectedIndexes[3] },
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

@Preview(showBackground = true)
@Composable
fun ParagraphsSelectorAndTextColor() {
    val selectedIndex = remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Card(
            modifier = Modifier
                .height(32.dp)
                .weight(1f),
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
                    tint = MaterialTheme.colorScheme.secondary,
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
                    .clickable { selectedIndex.intValue = 0 },
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 0.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndex.intValue == 0) MaterialTheme.colorScheme.primary
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
                        if (selectedIndex.intValue == 0) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable { selectedIndex.intValue = 1 },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndex.intValue == 1) MaterialTheme.colorScheme.primary
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
                        if (selectedIndex.intValue == 1) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable { selectedIndex.intValue = 2 },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndex.intValue == 2) MaterialTheme.colorScheme.primary
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
                        if (selectedIndex.intValue == 2) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(1.dp))

            Card(
                modifier = Modifier
                    .height(32.dp)
                    .weight(1f)
                    .clickable { selectedIndex.intValue = 3 },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 12.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 12.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (selectedIndex.intValue == 3) MaterialTheme.colorScheme.primary
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
                        if (selectedIndex.intValue == 3) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}