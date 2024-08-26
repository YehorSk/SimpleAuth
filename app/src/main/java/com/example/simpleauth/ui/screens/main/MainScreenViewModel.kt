package com.example.simpleauth.ui.screens.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleauth.todo.data.model.Todo
import com.example.simpleauth.todo.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//sealed interface MainUiState{
//    data class Success(val todos: List<Todo>) : MainUiState
//    object Loading: MainUiState
//    data class Error(val message: String): MainUiState
//}


sealed interface MainUiState{
    data class Success(val todos: List<Todo>) : MainUiState
    object Loading: MainUiState
    object Error: MainUiState
}

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel(){

    var mainUiState : MainUiState by mutableStateOf(MainUiState.Loading)
        private set

    init{
        getTodos()
    }

    fun getTodos() {
        viewModelScope.launch {
            delay(3000L)
            mainUiState = try{
                MainUiState.Success(todoRepository.getTodos())
            }catch (e: Exception){
                Log.v("TODOS", e.message.toString())
                MainUiState.Error
            }
        }
    }


}