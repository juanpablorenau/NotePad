package com.example.notepad.navigation

sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash_screen")
    data object NotesScreen : AppScreens("notes_screen") }
