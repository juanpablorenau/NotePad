package com.example.notepad.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.enums.Language
import com.example.notepad.R
import com.example.notepad.theme.DarkSkyBlue
import com.example.notepad.theme.LightSkyBlue
import com.example.notepad.utils.getViewModel

@Composable
fun SettingsScreen(
    openDrawer: () -> Unit = {},
    isDarkTheme: Boolean = false,
    language: Language = Language.EN
) {
    val viewModel = LocalContext.current.getViewModel<SettingsViewModel>()

    Scaffold(
        topBar = {
            SettingsTopBar(openDrawer)
        },
        content = { padding ->
            SettingsContent(
                padding = padding,
                isDarkTheme = isDarkTheme,
                language = language,
                changeDarkTheme = { viewModel.setIsDarkTheme(!isDarkTheme) },
                changeLanguage = { languageKey -> viewModel.setLanguage(languageKey) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(openDrawer: () -> Unit = {}) {
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

@Preview(showBackground = true)
@Composable
fun SettingsContent(
    padding: PaddingValues = PaddingValues(),
    isDarkTheme: Boolean = false,
    language: Language = Language.EN,
    changeDarkTheme: () -> Unit = {},
    changeLanguage: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        DarkModeSwitch(
            isDarkTheme = isDarkTheme,
            changeDarkTheme = changeDarkTheme
        )

        LanguageSelector(language = language, changeLanguage = changeLanguage)
    }
}

@Preview(showBackground = true)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LanguageSelector(
    language: Language = Language.EN,
    languages: List<Language> = Language.entries,
    label: String = stringResource(R.string.language),
    changeLanguage: (String) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            value = language.fullName,
            onValueChange = {},
            label = {
                Text(text = label)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { language: Language ->
                DropdownMenuItem(
                    text = {
                        Text(text = language.fullName, fontSize = 16.sp)
                    },
                    onClick = {
                        expanded = false
                        changeLanguage(language.key)
                    }
                )
            }
        }
    }
}