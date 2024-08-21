package com.example.simpleauth.todo.data.repository

import com.example.simpleauth.todo.data.model.Todo

interface TodoRepository {

    suspend fun getTodos(): List<Todo>

}