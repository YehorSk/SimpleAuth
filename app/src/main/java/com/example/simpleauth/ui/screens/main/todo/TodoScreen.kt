package com.example.simpleauth.ui.screens.main.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simpleauth.ui.screens.common.ErrorScreen
import com.example.simpleauth.ui.screens.common.LoadingScreen
import com.example.simpleauth.ui.screens.main.MainScreenViewModel
import com.example.simpleauth.ui.screens.main.MainUiState
import com.example.simpleauth.ui.screens.main.components.TodoList

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
){
    Column(
        modifier = modifier
    ) {
        when(val uiState = viewModel.mainUiState){
            is MainUiState.Error -> ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                onRetryAction = viewModel::getTodos
            )
            is MainUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is MainUiState.Success -> TodoList(todos = uiState.todos,modifier = Modifier.fillMaxSize())
        }
    }
}