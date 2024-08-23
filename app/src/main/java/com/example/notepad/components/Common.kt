package com.example.notepad.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.NoteCheckBox
import com.example.model.entities.NoteTextField
import com.example.notepad.R
import com.example.notepad.theme.DarkGray

@Composable
fun Chip(text: String = "Chip", chipColor: Color = Color.White, textColor: Color = Color.Black) {
    Card(
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors().copy(containerColor = chipColor),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontSize = 10.sp,
                text = text,
                color = textColor,
            )
        }
    }
}


@Composable
fun MenuItem(
    icon: Int = R.drawable.ic_delete_outline,
    text: String = "Delete",
    iconColor: Color = DarkGray,
    textColor: Color = DarkGray,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Menu icon",
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            color = textColor
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun Dialog(text: String = "Question?", yesAction: () -> Unit = {}, noAction: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = { },
        text = { Text(text) },
        confirmButton = {
            Text(
                modifier = Modifier.clickable { yesAction() },
                text = stringResource(R.string.accept),
            )
        },
        dismissButton = {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { noAction() },
                text = stringResource(R.string.cancel),
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    checkBox: NoteCheckBox = NoteCheckBox(text = "Sample Text"),
    iconAction: (String) -> Unit = {},
) {
    var isChecked by remember { mutableStateOf(checkBox.isChecked) }
    var checkboxText by remember { mutableStateOf(TextFieldValue(checkBox.text)) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 24.dp)
            .focusRequester(focusRequester),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            BasicTextField(
                value = checkboxText,
                singleLine = true,
                onValueChange = { newText -> checkboxText = newText },
            )
        }

        Icon(
            modifier = Modifier.clickable { iconAction(checkBox.id) },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Close icon",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(noteItem: NoteTextField = NoteTextField(text = "Sample Text")) {
    var text by remember { mutableStateOf(noteItem.text) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .focusRequester(focusRequester),
            value = text,
            onValueChange = { newText ->
                text = newText
            },
        )
    }
}
