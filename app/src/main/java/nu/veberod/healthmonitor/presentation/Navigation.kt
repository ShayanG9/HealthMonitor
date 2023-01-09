package nu.veberod.healthmonitor.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nu.veberod.healthmonitor.presentation.screens.Overview
import nu.veberod.healthmonitor.presentation.screens.Settings

@Composable
fun Navigation(setVisibility: (Boolean) -> Unit) {
    val navController = rememberNavController()

    // NAVHOST to keep track of composables
    NavHost(navController = navController, startDestination = Screen.Overview.route) {
        composable(route = Screen.Overview.route) {
            setVisibility(true)
            Overview(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            setVisibility(false)
            Settings(navController = navController)
        }
    }
}