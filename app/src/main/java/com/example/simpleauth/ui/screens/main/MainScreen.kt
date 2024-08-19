package com.example.simpleauth.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(){
//    val viewModel : MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
//    val uiState = viewModel.mainUiState
//    Scaffold { innerPadding ->
//        when(uiState){
//            is MainUiState.Success -> TodoList(todos = uiState.todos, modifier = Modifier.padding(innerPadding).fillMaxSize())
//            is MainUiState.Loading -> Text(text = "Loading")
//            is MainUiState.Error -> Text(text = uiState.message, modifier = Modifier.padding(16.dp))
//        }
//    }
}