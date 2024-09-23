package com.example.notepad.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notepad.R


@Preview(showBackground = true)
@Composable
fun NoteDetailBottomBar(
    addTextField: () -> Unit = {},
    addCheckBox: (String?) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_format),
            contentDescription = "text format icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            modifier = Modifier.clickable { addCheckBox(null) },
            painter = painterResource(id = R.drawable.ic_check_box),
            contentDescription = "check box icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            modifier = Modifier.clickable { addTextField() },
            painter = painterResource(id = R.drawable.ic_text_fields),
            contentDescription = "text fields icon",
            tint = MaterialTheme.colorScheme.primary
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = "Image icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}