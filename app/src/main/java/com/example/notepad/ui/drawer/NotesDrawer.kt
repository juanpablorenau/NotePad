package com.example.notepad.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.notepad.R
import com.example.notepad.navigation.AppNavigation
import com.example.notepad.navigation.AppScreens
import kotlinx.coroutines.launch

@Composable
fun DrawerNotes() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val openDrawer: () -> Unit = { coroutineScope.launch { drawerState.open() } }

    val navigateToNotes: () -> Unit = {
        coroutineScope.launch {
            navController.popBackStack(AppScreens.NotesScreen.route, false)
            drawerState.close()
        }
    }

    val navigateToSettings: () -> Unit = {
        coroutineScope.launch {
            navController.navigate(AppScreens.SettingsScreen.route)
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navigateToNotes = navigateToNotes,
                navigateToSettings = navigateToSettings
            )
        },
        content = {
            AppNavigation(
                navController = navController,
                openDrawer = openDrawer
            )
        }
    )
}


@Composable
fun DrawerContent(
    navigateToNotes: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f),
        drawerContainerColor = MaterialTheme.colorScheme.tertiary,
        content = {
            DrawerSheetContent(
                navigateToNotes = navigateToNotes,
                navigateToSettings = navigateToSettings
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DrawerSheetContent(
    navigateToNotes: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
) {
    Column {
        DrawerHeader()
        DrawerBody(
            navigateToNotes = navigateToNotes,
            navigateToSettings = navigateToSettings
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerHeader() {
    Column {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.ic_edit_note_icon_foreground),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "App icon"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 24.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }

}

data class DrawerItem(
    val icon: Painter,
    val text: String = "",
    val selected: Boolean = false,
    val onClick: () -> Unit = {},
)

@Preview(showBackground = true)
@Composable
fun DrawerBody(
    navigateToNotes: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {

        val drawerItems = listOf(
            DrawerItem(
                icon = painterResource(id = R.drawable.ic_sticky_note),
                text = stringResource(id = R.string.notes_title),
                onClick = navigateToNotes
            ),
            DrawerItem(
                icon = painterResource(id = R.drawable.ic_settings),
                text = stringResource(id = R.string.settings),
                onClick = navigateToSettings
            ),
        )

        drawerItems.forEach{ item ->
            DrawerItemComponent(
                icon = item.icon,
                text = item.text,
                selected = item.selected,
                onClick = item.onClick
            )

        }
    }
}

@Composable
fun DrawerItemComponent(
    icon: Painter,
    text: String = "",
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    NavigationDrawerItem(
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.tertiary,
        ),
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Drawer icon"
            )
        },
        label = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        },
        selected = selected,
        onClick = { onClick() }
    )
}