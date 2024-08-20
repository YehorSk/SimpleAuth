package com.example.simpleauth.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleauth.auth.AuthResult
import com.example.simpleauth.ui.screens.auth.AuthScreenViewModel
import com.example.simpleauth.ui.screens.auth.LoginScreen
import com.example.simpleauth.ui.screens.auth.RegisterScreen
import com.example.simpleauth.ui.screens.main.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    val authViewModel : AuthScreenViewModel = hiltViewModel()
    val context = LocalContext.current
    Scaffold { innerPadding->
        NavHost(
            navController = navController,
            startDestination = RegistrationScreen
        ){
            composable<RegistrationScreen>{
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = authViewModel,
                    onLogClick = {navController.navigate(LoginScreen)}
                )
            }
            composable<LoginScreen> {
                LoginScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = authViewModel
                )
            }
            composable<MainScreenGraph> {
                MainScreen(
                    modifier = Modifier.fillMaxSize(),
                    logOutButton = {
                        authViewModel.logout()
                        navController.navigate(RegistrationScreen)
                    }
                    )
            }
        }
    }
    LaunchedEffect(authViewModel,context) {
        authViewModel.authResults.collect{ result ->
            when(result){
                is AuthResult.Authorized -> {
                    Toast.makeText(context,"Authorized", Toast.LENGTH_LONG).show()
                    navController.navigate(MainScreenGraph)
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context,"Unauthorized", Toast.LENGTH_LONG).show()
                    navController.navigate(RegistrationScreen)
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context,"UnknownError", Toast.LENGTH_LONG).show()
                    navController.navigate(RegistrationScreen)
                }
            }
        }
    }
}

@Serializable
object RegistrationScreen

@Serializable
object LoginScreen

@Serializable
object MainScreenGraph