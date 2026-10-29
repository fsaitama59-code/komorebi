package com.komorebi.reader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.komorebi.reader.ui.screens.LibraryScreen
import com.komorebi.reader.ui.screens.ReaderScreen
import com.komorebi.reader.ui.screens.SettingsScreen
import com.komorebi.reader.ui.screens.ExploreScreen

/**
 * Komorebi Navigation System
 * Liquid/glass navigation with smooth transitions
 */

@Composable
fun KomorebiNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    NavHost(navController, startDestination = "library") {
        composable("library") {
            LibraryScreen(onOpenReader = { bookId, chapterId ->
                navController.navigate("reader/$bookId/$chapterId")
            })
        }

        composable(
            route = "reader/{bookId}/{chapterId}",
            arguments = listOf(
                androidx.navigation.navArgument("bookId") { type = androidx.navigation.NavType.StringType },
                androidx.navigation.navArgument("chapterId") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.getString()!!
            val chapterId = backStackEntry.getString()!!
            ReaderScreen(
                bookId = bookId,
                chapterId = chapterId,
                onNavigateUp = { navController.popBackStack() }
            )
        }

        composable("explore") {
            ExploreScreen(onOpenReader = { bookId, chapterId ->
                navController.navigate("reader/$bookId/$chapterId")
            })
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}

// ===== BOTTOM NAVIGATION ITEMS =====

val BottomNavItems = listOf(
    NavBarItem(
        icon = { /* Library icon */ },
        label = "Bibliothèque",
        route = "library"
    ),
    NavBarItem(
        icon = { /* Explore icon */ },
        label = "Explorer",
        route = "explore"
    ),
    NavBarItem(
        icon = { /* Settings icon */ },
        label = "Réglages",
        route = "settings"
    )
)