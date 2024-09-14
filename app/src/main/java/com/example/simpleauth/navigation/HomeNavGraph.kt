package com.example.simpleauth.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.simpleauth.ui.screens.main.settings.SettingsScreen
import com.example.simpleauth.ui.screens.main.todo.TodoScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLogOutClicked: () -> Unit
){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.Todo.route
    ){
        composable(HomeScreen.Todo.route) {
            TodoScreen(
                modifier = modifier.fillMaxSize()
            )
        }
        composable(HomeScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onLogOutClicked = onLogOutClicked
            )
        }
    }
}


sealed class HomeScreen(val route: String){
    object Todo: HomeScreen(route = "TODO")
    object Settings: HomeScreen(route = "SETTINGS")
}
