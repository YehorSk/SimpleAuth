package com.example.simpleauth.service

import com.example.simpleauth.todo.data.model.Todo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TodoService {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("todo")
    suspend fun getTodos(@Header("Authorization") token: String) : List<Todo>

}