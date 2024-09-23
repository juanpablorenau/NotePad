package com.example.notepad.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepad.R
import com.example.notepad.theme.DarkSkyBlue
import com.example.notepad.theme.LightSkyBlue

@Preview(showBackground = true)
@Composable
fun SettingsScreen(
    openDrawer: () -> Unit = {},
    isDarkTheme: Boolean = false,
    changeDarkTheme: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            SettingsTopBar(openDrawer)
        },
        content = { padding ->
            SettingsContent(
                padding = padding,
                isDarkTheme = isDarkTheme,
                changeDarkTheme = changeDarkTheme
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(openDrawer: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = { openDrawer() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.settings),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {}
    )
}

@Composable
fun SettingsContent(
    padding: PaddingValues = PaddingValues(),
    isDarkTheme: Boolean = false,
    changeDarkTheme: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(padding)
    ) {
        DarkModeSwitch(
            isDarkTheme = isDarkTheme,
            changeDarkTheme = changeDarkTheme
        )
    }
}

@Composable
fun DarkModeSwitch(
    isDarkTheme: Boolean = true,
    changeDarkTheme: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.dark_mode),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
        )
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { changeDarkTheme() },
            colors = SwitchDefaults.colors(
                checkedBorderColor = MaterialTheme.colorScheme.onBackground,
                uncheckedBorderColor = MaterialTheme.colorScheme.onBackground,
                checkedThumbColor = DarkSkyBlue,
                uncheckedThumbColor = LightSkyBlue,
                checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
            ),
            thumbContent = {
                if (isDarkTheme) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        painter = painterResource(id = R.drawable.ic_moon),
                        contentDescription = "Dark mode icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        modifier = Modifier.padding(2.dp),
                        painter = painterResource(id = R.drawable.ic_sun),
                        contentDescription = "Light mode icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}
