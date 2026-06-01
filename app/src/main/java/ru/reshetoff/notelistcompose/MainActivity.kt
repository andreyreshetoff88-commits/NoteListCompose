package ru.reshetoff.notelistcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.reshetoff.common.State
import ru.reshetoff.login_presentation.ui.LoginScreen
import ru.reshetoff.notelistcompose.navigation.MainScreenWithBottomBar
import ru.reshetoff.notelistcompose.navigation.Screen
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
    val context = LocalContext.current
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf(Screen.Groups.route) }
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkToken().collect { hasToken ->
            startDestination = if (hasToken) {
                viewModel.sync()
                Screen.Groups.route
            } else {
                Screen.Login.route
            }
        }
    }

    LaunchedEffect(syncState) {
        when (syncState) {
            is State.Loading -> {}
            is State.Success -> {
                if (!syncState.data!!) {

                }
            }

            is State.Error -> Toast.makeText(
                context,
                syncState.message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
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
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Groups.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        //Main Screen
        composable(Screen.Groups.route) {
            MainScreenWithBottomBar(syncState)
        }
    }
}