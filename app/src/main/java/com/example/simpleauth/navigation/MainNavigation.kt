package com.example.simpleauth.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpleauth.ui.screens.login.LoginScreen
import com.example.simpleauth.ui.screens.login.LoginScreenViewModel
import com.example.simpleauth.ui.screens.register.RegisterScreen
import com.example.simpleauth.ui.screens.register.RegisterScreenViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    val regViewModel : RegisterScreenViewModel = hiltViewModel()
    val logViewModel : LoginScreenViewModel = hiltViewModel()
    Scaffold { innerPadding->
        NavHost(
            navController = navController,
            startDestination = RegistrationScreen
        ){
            composable<RegistrationScreen>{
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = regViewModel,
                    onLogClick = {navController.navigate(LoginScreen)}
                )
            }
            composable<LoginScreen> {
                LoginScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = logViewModel
                )
            }
        }
    }
}

@Serializable
object RegistrationScreen

@Serializable
object LoginScreen