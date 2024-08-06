package com.example.notepad.navigation

sealed class AppScreens(val route: String) {
    data object NotesScreen : AppScreens("notes_screen")
    data object NoteDetailScreen : AppScreens("note_detail_screen")
}
