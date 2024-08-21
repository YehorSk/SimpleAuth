package com.example.simpleauth.service

import com.example.simpleauth.auth.data.model.HttpResponse
import com.example.simpleauth.todo.data.model.Todo
import com.example.simpleauth.ui.screens.auth.LoginForm
import com.example.simpleauth.ui.screens.auth.RegisterForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("register")
    suspend fun register(@Body registerForm: RegisterForm) : HttpResponse

    @POST("login")
    suspend fun login(@Body loginForm: LoginForm) : HttpResponse

    @GET("logout")
    suspend fun logout(@Header("Authorization") token: String) : HttpResponse

    @GET("user")
    suspend fun authenticate(@Header("Authorization") token: String) : HttpResponse

}