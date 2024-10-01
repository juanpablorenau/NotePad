package com.example.notepad.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

            LaunchedEffect(Unit) { viewModel.isDarkTheme.collect { isDarkTheme.value = it } }

            NotePadTheme(isDarkTheme.value) { DrawerNotes(isDarkTheme.value) }
        }
    }
}
