package ru.reshetoff.notelistcompose.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Groups : Screen("groups")
    object Profile : Screen("profile")
    object Verification : Screen("verification")
}

sealed class BottomNavScreen(
    val route: String,
    val title: String
) {
    object Groups : BottomNavScreen("groups", "Группы")
    object Profile : BottomNavScreen("profile", "Профиль")
}