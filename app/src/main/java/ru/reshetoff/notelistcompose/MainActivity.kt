package ru.reshetoff.notelistcompose

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.reshetoff.login_presentation.ui.LoginScreen
import ru.reshetoff.notelistcompose.navigation.MainScreenWithBottomBar
import ru.reshetoff.notelistcompose.navigation.Screen
import ru.reshetoff.notelistcompose.ui.MainViewModel
import ru.reshetoff.notelistcompose.ui.theme.NoteListComposeTheme
import ru.reshetoff.register_presentation.ui.RegisterScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteListComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFF4E0)
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val startDestination = if (viewModel.checkToken()) {
        Screen.Groups.route
    } else {
        Screen.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {

        //Login screen
        composable(Screen.Login.route) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Groups.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        //Register screen
        composable(Screen.Register.route) {
            RegisterScreen()
        }

        //Main Screen
        composable(Screen.Groups.route) {
            MainScreenWithBottomBar()
        }
    }
}