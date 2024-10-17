package com.example.notepad.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.model.enums.Language
import com.example.notepad.ui.detail.NoteDetailScreen
import com.example.notepad.ui.list.NotesScreen
import com.example.notepad.ui.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    openDrawer: () -> Unit,
    isDarkTheme: Boolean = false,
    language: Language = Language.EN
) {
    Surface {
        NavHost(
            navController = navController,
            startDestination = AppScreens.NotesScreen.route
        ) {
            composable(route = AppScreens.NotesScreen.route) {
                NotesScreen(
                    navController = navController,
                    openDrawer = { openDrawer() },
                    isDarkTheme = isDarkTheme,
                )
            }

            composable(
                route = AppScreens.NoteDetailScreen.route + "/{noteId}" + "/{index}",
                arguments = listOf(
                    navArgument(name = "noteId") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    },
                    navArgument(name = "index") {
                        type = NavType.IntType
                        defaultValue = 0
                        nullable = false
                    }
                )
            ) {
                val noteId = it.arguments?.getString("noteId").orEmpty()
                val index = it.arguments?.getInt("index") ?: 0
                NoteDetailScreen(
                    navController = navController,
                    noteId = noteId,
                    index = index,
                    isDarkTheme = isDarkTheme
                )
            }

            composable(route = AppScreens.SettingsScreen.route) {
                SettingsScreen(
                    openDrawer = { openDrawer() },
                    isDarkTheme = isDarkTheme,
                    language = language
                )
            }
        }
    }
}

