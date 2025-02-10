package com.mydoctor.ramcharacters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mydoctor.ramcharacters.navigation.RAMNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun RAMApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier,
) {
    RAMNavHost(
        navController = navController,
        modifier = modifier,
    )
}