package com.example.notepad.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.notepad.theme.NotePadTheme
import com.example.notepad.ui.drawer.DrawerNotes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(isSystemInDarkTheme)}
            val changeDarkTheme = { isDarkTheme = !isDarkTheme }

            NotePadTheme(isDarkTheme) { DrawerNotes(isDarkTheme) { changeDarkTheme() } }
        }
    }
}
