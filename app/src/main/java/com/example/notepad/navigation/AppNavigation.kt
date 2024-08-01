package com.example.notepad.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notepad.ui.list.NotesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = AppScreens.NotesScreen.route
    ) {
        composable(route = AppScreens.NotesScreen.route) { NotesScreen() }
    }
}
