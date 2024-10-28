package com.example.notepad.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R
import kotlinx.coroutines.launch

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
    iconColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = MaterialTheme.colorScheme.secondary,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = icon), contentDescription = "Menu icon", tint = iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text, overflow = TextOverflow.Ellipsis, color = textColor
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Composable
fun Dialog(text: String = "Question?", yesAction: () -> Unit = {}, noAction: () -> Unit = {}) {
    AlertDialog(onDismissRequest = { }, text = { Text(text) }, confirmButton = {
        Text(
            modifier = Modifier.clickable { yesAction() },
            text = stringResource(R.string.accept),
        )
    }, dismissButton = {
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { noAction() },
            text = stringResource(R.string.cancel),
        )
    })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DisplayText(
    description: Int = R.string.pin, content: @Composable () -> Unit = {}
) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    val scope = rememberCoroutineScope()

    TooltipBox(
        focusable = false,
        state = tooltipState,
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            Chip(
                text = stringResource(description),
                chipColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onBackground
            )
        },
        modifier = Modifier.combinedClickable(onClick = {}, onLongClick = {
            scope.launch {
                tooltipState.show()
            }
        }),
    ) {
        content()
    }
}
