package com.example.notepad.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import com.example.model.entities.Language
import com.example.notepad.theme.NotePadTheme
import com.example.notepad.ui.drawer.DrawerNotes
import com.example.notepad.utils.getViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = getViewModel<MainViewModel>()

        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }
            val language = remember { mutableStateOf(Language.EN) }

            LaunchedEffect(Unit) {
                viewModel.isDarkTheme.collect { isDarkTheme.value = it }
            }

            LaunchedEffect(Unit) {
                viewModel.language.collect { currentLanguage ->
                    language.value = currentLanguage
                    val appLocale = LocaleListCompat.forLanguageTags(language.value.key)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }

            NotePadTheme(isDarkTheme.value) { DrawerNotes(isDarkTheme.value, language.value) }
        }
    }
}
