package com.example.notepad.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.model.enums.Language
import com.example.notepad.R
import com.example.notepad.navigation.AppNavigation
import com.example.notepad.navigation.AppScreens
import kotlinx.coroutines.launch

@Composable
fun DrawerNotes(
    isDarkTheme: Boolean = false,
    language: Language = Language.EN,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val openDrawer: () -> Unit = { coroutineScope.launch { drawerState.open() } }

    val navigateToNotes: () -> Unit = {
        coroutineScope.launch {
            drawerState.close()
            navController.popBackStack(AppScreens.NotesScreen.route, false)
        }
    }

    val navigateToSettings: () -> Unit = {
        coroutineScope.launch {
            drawerState.close()
            navController.navigate(AppScreens.SettingsScreen.route)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navigateToNotes = navigateToNotes,
                navigateToSettings = navigateToSettings,
            )
        },
        content = {
            AppNavigation(
                navController = navController,
                openDrawer = openDrawer,
                isDarkTheme = isDarkTheme,
                language = language
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
        modifier = Modifier.fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets.statusBars,
        drawerShape = RectangleShape,
        content = {
            DrawerSheetContent(
                navigateToNotes = navigateToNotes,
                navigateToSettings = navigateToSettings,
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
            navigateToSettings = navigateToSettings,
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )
    }

}

data class DrawerItem(
    val index: Int = 0,
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
    val drawerItems = listOf(
        DrawerItem(
            index = 0,
            icon = painterResource(id = R.drawable.ic_sticky_note),
            text = stringResource(id = R.string.notes_title),
            onClick = navigateToNotes
        ),
        DrawerItem(
            index = 1,
            icon = painterResource(id = R.drawable.ic_settings),
            text = stringResource(id = R.string.settings),
            onClick = navigateToSettings
        ),
    )

    var selectedItemId by rememberSaveable { mutableIntStateOf(drawerItems.first().index) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        drawerItems.forEach { item ->
            DrawerItemComponent(
                icon = item.icon,
                text = item.text,
                selected = item.index == selectedItemId,
                onClick = {
                    item.onClick()
                    selectedItemId = item.index
                }
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
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.background,
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