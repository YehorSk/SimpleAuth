package com.example.simpleauth.todo.data.repository

import android.util.Log
import com.example.simpleauth.auth.data.repository.AuthPreferencesRepository
import com.example.simpleauth.service.TodoService
import com.example.simpleauth.todo.data.model.Todo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoService: TodoService,
    private val prefs: AuthPreferencesRepository
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> {
        val token = prefs.jwtTokenFlow.first()
        val result = todoService.getTodos("Bearer $token")
        return result
    }

}