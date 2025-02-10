package com.mydoctor.ramcharacters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mydoctor.ramcharacters.ui.characters.CharactersDestination
import com.mydoctor.ramcharacters.ui.characters.CharactersScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun RAMNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CharactersDestination.route,
        modifier = modifier
    ) {
        composable(route = CharactersDestination.route) {
            CharactersScreen()
        }
    }
}
