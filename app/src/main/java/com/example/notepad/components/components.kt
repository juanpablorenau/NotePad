package com.example.notepad.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R
import com.example.notepad.theme.YellowDark

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
fun MenuItem(icon: Int = R.drawable.ic_delete_outline, text: String = "Delete") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Menu icon",
            tint = YellowDark
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
        )
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
                modifier = Modifier.padding(end = 16.dp).clickable { noAction() },
                text = stringResource(R.string.cancel),
            )
        }
    )
}