package nu.veberod.healthmonitor.presentation

sealed class Screen(val route: String) {
    object Overview : Screen(route = "overview")
    object Settings : Screen(route = "settings")
}
