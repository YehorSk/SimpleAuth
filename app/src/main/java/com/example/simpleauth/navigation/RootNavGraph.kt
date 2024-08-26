package com.example.simpleauth.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleauth.auth.data.model.AuthResult
import com.example.simpleauth.ui.screens.auth.AuthScreenViewModel
import com.example.simpleauth.ui.screens.main.home.HomeScreen

@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val context = LocalContext.current
    val authViewModel : AuthScreenViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(
            navController = navController
        )

        composable(route = Graph.HOME) {
            HomeScreen(
                onLogOutClicked = {
                    authViewModel.logout()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }

    LaunchedEffect(authViewModel.authResults, context) {
        authViewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    Toast.makeText(context, "Authorized", Toast.LENGTH_LONG).show()
                    navController.navigate(Graph.HOME) {
                        popUpTo(AuthScreen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_LONG).show()
                    val currentRoute = navController.currentDestination?.route
                    Log.v("ROUTE",currentRoute.toString())
                    navController.navigate(AuthScreen.SignUp.route) {
                        popUpTo(AuthScreen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context, result.data?.message.toString(), Toast.LENGTH_LONG).show()
                    navController.navigate(AuthScreen.SignUp.route) {
                        popUpTo(AuthScreen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}
