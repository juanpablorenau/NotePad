package com.example.notepad.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.model.utils.orEmptyString
import com.example.notepad.ui.detail.NoteDetailScreen
import com.example.notepad.ui.list.NotesScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(
            navController = navController, startDestination = AppScreens.NotesScreen.route
        ) {
            composable(route = AppScreens.NotesScreen.route) {
                NotesScreen(
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this
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
                val noteId = it.arguments?.getString("noteId").orEmptyString()
                val index = it.arguments?.getInt("index") ?: 0
                NoteDetailScreen(
                    navController = navController,
                    noteId = noteId,
                    index = index,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this
                )
            }
        }
    }
}
