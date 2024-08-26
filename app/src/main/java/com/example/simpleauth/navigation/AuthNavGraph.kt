package com.example.simpleauth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.simpleauth.ui.screens.auth.LoginScreen
import com.example.simpleauth.ui.screens.auth.RegisterScreen
import kotlinx.coroutines.launch



fun NavGraphBuilder.authNavGraph(
    navController: NavController
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.SignUp.route
    ){

        composable(AuthScreen.SignUp.route){
            RegisterScreen(
                onLogClick = { navController.navigate(AuthScreen.Login.route) }
            )
        }

        composable(AuthScreen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(AuthScreen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }
}

sealed class AuthScreen(val route: String){
    object Login: AuthScreen(route = "LOGIN")
    object SignUp: AuthScreen(route = "SIGN_UP")
}

