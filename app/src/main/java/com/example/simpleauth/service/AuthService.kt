package com.example.simpleauth.service

import com.example.simpleauth.auth.AuthResult
import com.example.simpleauth.model.HttpResponse
import com.example.simpleauth.model.Todo
import com.example.simpleauth.model.User
import com.example.simpleauth.ui.screens.login.LoginForm
import com.example.simpleauth.ui.screens.register.RegisterForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @GET("todo")
    suspend fun getAllTodos(): List<Todo>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("register")
    suspend fun register(@Body registerForm: RegisterForm) : HttpResponse

    @POST("login")
    suspend fun login(@Body loginForm: LoginForm) : HttpResponse

    @POST("logout")
    suspend fun logout(@Body loginForm: LoginForm) : HttpResponse

    @GET("user")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ) : User

}