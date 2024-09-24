package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            painter = painterResource(id = R.drawable.ic_text_format),
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
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
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
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    modifier = Modifier.clickable {
                        showBottomSheet.value = false
                        keyboardController?.show()
                    },
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "close icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),

                ) {

            }

        }
    }
}