package com.example.simpleauth.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpleauth.ui.screens.main.components.TodoList

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    logOutButton: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
    ){
    Column(
        modifier = modifier
    ) {
        Text(text = "You're authorized")
        Button(onClick = logOutButton) {
            Text(text = "LogOut")
        }
        when(val uiState = viewModel.mainUiState){
            is MainUiState.Error -> Text(text = "Error")
            is MainUiState.Loading -> Text(text = "Loading")
            is MainUiState.Success -> TodoList(todos = uiState.todos,modifier = modifier)
        }
    }
}